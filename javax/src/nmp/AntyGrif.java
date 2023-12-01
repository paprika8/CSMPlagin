package nmp;

import arc.Events;
import arc.func.Cons;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.production.GenericCrafter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AntyGrif {
    ArrayList<String> uuids = new ArrayList<>();
    ArrayList<String> grif = new ArrayList<>();
    String focus = null;
    File banUUID;
    // сделать фокус и /y /n
    final Cons<EventType.BlockBuildBeginEvent> event = blockBuildBeginEvent -> {
        if(!blockBuildBeginEvent.unit.isPlayer())
            return;
        if(!blockBuildBeginEvent.breaking){
            if(((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof GenericCrafter
            || ((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof Turret
            || ((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof PowerGenerator){
                uuids.remove(blockBuildBeginEvent.unit.getPlayer().uuid());
                grif.remove(blockBuildBeginEvent.unit.getPlayer().uuid());
            }
        }
        boolean hasUUID = false;
        for(String uuid : uuids)
            if(uuid.equals(blockBuildBeginEvent.unit.getPlayer().uuid()))
                hasUUID = true;
        if(!hasUUID)
            return;
        if((((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof GenericCrafter
                || ((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof Turret
                || ((ConstructBlock.ConstructBuild)blockBuildBeginEvent.tile.build).current instanceof PowerGenerator)
                && Vars.state.wave > 2
        ){
            uuids.remove(blockBuildBeginEvent.unit.getPlayer().uuid());
            grif.add(blockBuildBeginEvent.unit.getPlayer().uuid());
            blockBuildBeginEvent.unit.getPlayer().sendMessage("grifer?");
        }
    };
    AntyGrif(){
        try {
            Path path;
            Path buff = Paths.get("/root/mods");
            if (!Files.exists(buff))
                path = Files.createDirectory(buff);
            else path = buff;

            buff = Paths.get("/root/mods/passwords");
            if (!Files.exists(buff))
                path = Files.createDirectory(buff);
            else path = buff;

            buff = Paths.get("/root/mods/passwords/antygrif.b");
            if (!Files.exists(buff))
                path = Files.createFile(buff);
            else path = buff;
            banUUID = new File(path.toUri());
        }catch (IOException e){
            e.printStackTrace();
        }
        Events.on(EventType.PlayerConnect.class, e -> {
            try{
                InputStream in = Files.newInputStream(banUUID.toPath(), StandardOpenOption.READ);
                while (in.available() != 0){
                    byte[] len = new byte[1];
                    in.read(len);
                    byte[] buff = new byte[len[0]];
                    in.read(buff);
                    if(new String(buff).equals(e.player.uuid())){
                        e.player.kick("antygrif");
                        return;
                    }
                }
            }
            catch (Exception ex){
                Log.warn("AntiGrif ist kaputt");
            }
            uuids.add(e.player.uuid());
            Timer timer = new Timer("grif Timer", true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean hasUUID = false;
                    for(String uuid : grif)
                        if(uuid.equals(e.player.uuid()))
                            hasUUID = true;
                    if(hasUUID)
                        e.player.kick("AntiGrif");
                    timer.cancel();
                }
            }, 60000);
        });
        Events.on(EventType.PlayerLeave.class, e -> {
            boolean hasUUID = false;
            for(int i = 0; i < grif.size(); i++){
                if(grif.get(i).equals(e.player.uuid()))
                    hasUUID = true;
            }
            if(hasUUID)
            {
                focus = e.player.uuid();
                Call.sendMessage(e.player.name + " был грифером? да/нет (напишите /y или /n соответственно)");
                Timer timer = new Timer("grif Timer", true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        focus = null;
                        timer.cancel();
                    }
                }, 20000);
                grif.remove(e.player.uuid());
                uuids.remove(e.player.uuid());
            }
        });
        Events.on(EventType.BlockBuildBeginEvent.class, event);
    }
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("y", "", "antygrif", (args, player) -> {
            if(focus == null)
                return;
            try{
                OutputStream out = Files.newOutputStream(banUUID.toPath(), StandardOpenOption.APPEND);
                out.write(focus.getBytes().length);
                out.write(focus.getBytes());
            }catch (IOException e){}
            focus = null;
        });
        handler.<Player>register("n", "", "antygrif", (args, player) -> {
            focus = null;
        });
    }
}
