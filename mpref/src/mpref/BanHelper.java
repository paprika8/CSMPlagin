package mpref;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.gen.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class BanHelper {
    File banUUID;
    BanHelper(){
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

            buff = Paths.get("/root/mods/passwords/bans.b");
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
                        e.player.kick("Ban");
                        return;
                    }
                }
            }
            catch (Exception ex){
                Log.warn("Ban ist kaputt");
            }
        });
    }
    void ban(String uuid, Player pl){
        try{
            InputStream in = Files.newInputStream(banUUID.toPath(), StandardOpenOption.READ);
            while (in.available() != 0){
                byte[] len = new byte[1];
                in.read(len);
                byte[] buff = new byte[len[0]];
                in.read(buff);
                if(new String(buff).equals(uuid)){
                    pl.sendMessage("Уже забанен");
                    return;
                }
            }

            OutputStream out = Files.newOutputStream(banUUID.toPath(), StandardOpenOption.APPEND);
            out.write(uuid.getBytes().length);
            out.write(uuid.getBytes());
        }catch (IOException e){
            pl.sendMessage("Ban ist kaputt");
            Log.warn("Ban ist kaputt");
            return;
        }
        pl.sendMessage("Готово");
    }
    void uban(String uuid, Player pl){
        ArrayList<String> arr = new ArrayList<>();
        try{
            InputStream in = Files.newInputStream(banUUID.toPath(), StandardOpenOption.READ);
            while (in.available() != 0){
                byte[] len = new byte[1];
                in.read(len);
                byte[] buff = new byte[len[0]];
                in.read(buff);
                if(new String(buff).equals(uuid)){
                    pl.sendMessage("Готово");
                }
                else{
                    arr.add(new String(buff));
                }
            }

            OutputStream out = Files.newOutputStream(banUUID.toPath(), StandardOpenOption.APPEND);
            for (String id:
                 arr) {
                out.write(id.getBytes().length);
                out.write(id.getBytes());
            }
        }
        catch (Exception ex){
            pl.sendMessage("Ban ist kaputt");
            Log.warn("Ban ist kaputt");
        }
    }
}
