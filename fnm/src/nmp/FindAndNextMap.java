package nmp;
import arc.Events;
import arc.files.Fi;
import arc.struct.Seq;
import arc.util.CommandHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.io.MapIO;
import mindustry.maps.Map;
import mindustry.mod.Plugin;
import mindustry.server.ServerControl;

public class FindAndNextMap extends Plugin {
    private static double ratio = 0.6;
    private HashSet<String> votes = new HashSet();
    private boolean enable = true;
    final String dir = System.getProperty("user.dir");
// xyzdiuP geue
    public FindAndNextMap() {
        Events.on(EventType.PlayerLeave.class, (e) -> {
            Player player = e.player;
            int cur = this.votes.size();
            int req = (int)Math.ceil(ratio * (double)Groups.player.size());
            if (this.votes.contains(player.uuid())) {
                this.votes.remove(player.uuid());
                Call.sendMessage("NMP: [accent]" + player.name + "[] ливнул, [green]" + cur + "[] проголосовало из, [green]" + req + "[] игроков");
            }
        });
        Events.on(EventType.GameOverEvent.class, (e) -> {
            this.votes.clear();
        });
        path = Paths.get(dir);
    }
    Path path;
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("nmp", "[off]", "Проведите голосование, чтобы изменить карту", (args, player) -> {
            if (player.admin()) {
                this.enable = args.length != 1 || !args[0].equals("off");
            }

            Iterator var5;
            Map map;
            if (args.length != 0 && args[0].equals("Vall")) {
                String s = "{";
                Seq<Map> mapsx = Vars.maps.all();

                for(var5 = mapsx.iterator(); var5.hasNext(); s = s + map.name() + ", ") {
                    map = (Map)var5.next();
                }

                s = s + "}";
                Call.sendMessage(s);
            } else {
                args[0] = args[0].replace('_', ' ');
                if (player.admin() && args.length != 0 && !args[0].equals("off")) {
                    Seq<Map> maps = Vars.maps.all();
                    boolean flag = false;
                    var5 = maps.iterator();

                    while(var5.hasNext()) {
                        map = (Map)var5.next();
                        if (args[0].equals(map.name())) {
                            ServerControl.instance.setNextMap(map);
                            Call.sendMessage("[green]Нашёл, поставил[]");
                            flag = true;
                        }
                    }

                    if (!flag) {
                        Call.sendMessage("[red]Нету такого[]");
                        return;
                    }
                }

                if (!this.enable) {
                    player.sendMessage("[red]NMP: nmap отключён =([]");
                } else {
                    this.votes.add(player.uuid());
                    int cur = this.votes.size();
                    int req = (int)Math.ceil(ratio * (double)Groups.player.size());
                    Call.sendMessage("NMP: [accent]" + player.name + "[] хочет изменить карту, [green]" + cur + "[] проголосовало из, [green]" + req + "[] имеющихся");
                    if (cur >= req) {
                        this.votes.clear();
                        Call.sendMessage("NMP: [green] голосование прошло успешно, меняем карту.");
                        Events.fire(new EventType.GameOverEvent(Team.crux));
                    }
                }
            }
        });
        handler.<Player>register("rmp", "[directory] [file]", "import map", (args, player) -> {
            if(args[0].equals("secret") && !player.admin)
            {
                player.sendMessage("[red]Нет прав");
                return;
            }
            Path p;
            if(!args[0].equals("0"))
                if(new File(Paths.get(path.toUri().getPath() + '/' + args[0]).toUri()).exists()){
                    p = Paths.get(path.toUri().getPath() + '/' + args[0]);
                }
                else {player.sendMessage("[red] (-_- )");return;}
            else
                p = path;
            if(!args[1].equals("0")){
                if(new File(Paths.get(p.toUri().getPath() + '/' + args[1] + ".msav").toUri()).exists()){
                    Fi f = new Fi(new File(Paths.get(p.toUri().getPath() + '/' + args[1] + ".msav").toUri()));
                    Map map;
                    try{map = MapIO.createMap(f, true);}
                    catch (IOException e){player.sendMessage("[red] (?_?)");return;}
                    ServerControl.instance.setNextMap(map);
                    player.sendMessage("[green] ( ^^)");
                    Events.fire(new EventType.GameOverEvent(player.team()));
                }
                else player.sendMessage("[red] (-_-*)");
            }
            else {
                File[] files = new File(p.toUri()).listFiles();
                try{
                    Seq<Map> maps = new Seq<Map>();
                    for (File f : files) {
                        try {
                            maps.add(MapIO.createMap(new Fi(f), true));
                        }
                        catch (IOException e) {
                            e.printStackTrace();player.sendMessage("[red] (?_?)");
                        }
                        catch (Exception e) {
                            player.sendMessage("[red] (?_?)");
                        }
                    }
                    if(!maps.isEmpty())
                        player.sendMessage("[green] ( ^^)");
                    else {player.sendMessage("[red] (-_-*)");return;}
                    ServerControl.instance.setNextMap(maps.get((int)(Math.random() * maps.size)));
                    Events.fire(new EventType.GameOverEvent(player.team()));
                }catch (Exception e) {
                    player.sendMessage("[red] (?_?)");
                }
            }

        });
        handler.<Player>register("dmp", "[directory]", "import map", (args, player) -> {
            if(args[0].equals("getDir")){
                player.sendMessage(path.toUri().toString());
                return;
            }
            if(args[0].equals("Back")){
                path = path.getParent();
                player.sendMessage(path.toUri().toString());
                return;
            }
            if(args[0].equals("secret"))
            {
                player.sendMessage("[red]Не надо так (при переходе в эту папку, к ней могут получить доступ другие)");
                return;
            }
            if(new File(Paths.get(path.toUri().getPath() + '/' + args[0]).toUri()).exists()){
                path = Paths.get(path.toUri().getPath() + '/' + args[0]);
                player.sendMessage("[green] =)");
            }
            else player.sendMessage("[red] (-_- )");
        });
    }
}
