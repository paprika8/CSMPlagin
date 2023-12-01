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
    File filePass, fileID;
    List<KeyPref> UUIDs = new ArrayList<>();
    final String dir = System.getProperty("user.dir");
    final BanHelper helper = new BanHelper();
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
            AccountMind.all.remove(AccountMind.ofPlayer(e.player));
        });
        Events.on(EventType.PlayerConnect.class, e -> {
            new AccountMind(e.player);
            for (KeyPref u : UUIDs){
                if(e.player.uuid().equals(u.key)){
                    if (AccountMind.ofPlayer(e.player) != null)
                        AccountMind.ofPlayer(e.player).setPrefix(u.prefix);
                    break;
                }
            }
            if(AccountMind.ofPlayer(e.player) != null){
                e.player.admin(!AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[0]));
                Log.info(e.player.uuid() + " зашёл как " + ((AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[0]))? "пользователь" : "админ"));
            }
            else {
                Log.err("Account", "Account is lost");
            }

        });
        Events.on(EventType.GameOverEvent.class, (e) -> {
            this.votes.clear();
        });
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

            buff = Paths.get("/root/mods/passwords/pass.b");
            if (!Files.exists(buff))
                path = Files.createFile(buff);
            else path = buff;
            filePass = new File(path.toUri());
            buff = Paths.get("/root/mods/passwords/id.b");
            if (!Files.exists(buff))
                path = Files.createFile(buff);
            else path = buff;
            fileID = new File(path.toUri());
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream stream = Files.newInputStream(Paths.get("/root/mods/passwords/id.b"));
            for (;stream.available() != 0;){
                byte[] buf, ubuf;
                int l = stream.read();
                ubuf = new byte[l];
                for(int y = 0; y < l; y++){
                    ubuf[y] = (byte)stream.read();
                }
                l = stream.read();
                buf = new byte[l];
                for(int y = 0; y < l; y++){
                    buf[y] = (byte)stream.read();
                }
                UUIDs.add(new KeyPref(decoding(ubuf, "abcd"),decoding(buf, "abcd")));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
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
        handler.<Player>register("lin", "[password] [key]", "login?", (args, player) -> {
            if(args.length < 2)return;
            List<KeyPref> Passs = getAllPass(args[1]);
            for (KeyPref kp: Passs) {
                if(args[0].equals(kp.key)){
                    if(AccountMind.ofPlayer(player) == null) {
                        player.sendMessage("не найденно");
                        return;
                    }
                    AccountMind.ofPlayer(player).setPrefix(kp.prefix);
                    Call.sendMessage("[green]Есть![]");
                    {
                        boolean is = false;
                        for (KeyPref key: UUIDs) {
                            if(player.uuid().equals(key.key)){
                                is = true; break;}
                        }
                        if(!is){
                            UUIDs.add(new KeyPref(player.uuid(), kp.prefix));
                        try{
                            OutputStream out = Files.newOutputStream(fileID.toPath(), StandardOpenOption.APPEND);
                            writeInFile(coding(player.uuid(), "abcd"), out);
                            writeInFile(coding(kp.prefix, "abcd"), out);
                        }catch (IOException e){}}
                    }
                    player.admin(!kp.prefix.equals("[U]"));
                    return;
                }
            }
            Call.sendMessage("[red]Неверный пароль[]");
        });
        handler.<Player>register("regp", "[password] [key] [pref]", "registration", (args, player) -> {
            if(args.length < 3)return;
            if(AccountMind.ofPlayer(player) == null) {
                player.sendMessage("не найденно");
                return;
            }
            if(AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[9]) || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[11])
                    || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[12])){
                try{
                    boolean is = false;
                    for (String k : AccountMind.prefixes)
                        if(args[2].equals(k))
                            is = true;
                    if(!is){Call.sendMessage("[red]нет такого[]"); return;}
                    OutputStream out = Files.newOutputStream(filePass.toPath(), StandardOpenOption.APPEND);
                    writeInFile(coding(args[0], args[1]), out);
                    writeInFile(coding(args[2], args[1]), out);
                    Call.sendMessage("[green]Есть![]");
                }catch (IOException e){}
            }
            else Call.sendMessage("[red]нет прав[]");
        });
        handler.<Player>register("cl_pref", "[off]", "registration", (args, player) -> {
            if(AccountMind.ofPlayer(player) == null) {
                player.sendMessage("не найденно");
                return;
            }
            if (AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[9]) || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[11])
                    || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[12]) || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[14])
                    || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[15]) || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[16])){
                UUIDs = new ArrayList<>();
                try {
                    InputStream stream = Files.newInputStream(fileID.toPath());
                    for (;stream.available() != 0;){
                        byte[] buf, ubuf;
                        int l = stream.read();
                        ubuf = new byte[l];
                        for(int y = 0; y < l; y++){
                            ubuf[y] = (byte)stream.read();
                        }
                        l = stream.read();
                        buf = new byte[l];
                        for(int y = 0; y < l; y++){
                            buf[y] = (byte)stream.read();
                        }
                        UUIDs.add(new KeyPref(decoding(ubuf, "abcd"),decoding(buf, "abcd")));
                    }
                }catch (IOException e){
                    e.printStackTrace();
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

        handler.removeCommand("ban");
        handler.removeCommand("uban");
        handler.<Player>register("ban", "[uuid]", "ban", (args, player) ->{
            if(player != null  && args.length != 0 && !args[0].isEmpty())
                helper.ban(args[0], player);
        });
        handler.<Player>register("uban", "[uuid]", "uban", (args, player) ->{
            if(player != null  && args.length != 0 && !args[0].isEmpty())
                helper.uban(args[0], player);
        });
    }
    private List<KeyPref> getAllPass(String key){
        List<KeyPref> Passs = new ArrayList<>();
        try{
            InputStream stream = Files.newInputStream(Paths.get("/root/mods/passwords/pass.b"));
            for (;stream.available() != 0;){
                byte[] buf, ubuf;
                int l = stream.read();
                ubuf = new byte[l];
                for(int y = 0; y < l; y++){
                    ubuf[y] = (byte)stream.read();
                }
                l = stream.read();
                buf = new byte[l];
                for(int y = 0; y < l; y++){
                    buf[y] = (byte)stream.read();
                }
                Passs.add(new KeyPref(decoding(ubuf, key),decoding(buf, key)));
            }
        }catch (IOException e){}
        return Passs;
    }
    private static byte[] coding(String str, String key){
        byte[] keyb = key.getBytes();
        int yor = keyb.length;
        int ik = 0;
        byte[] buff = str.getBytes();
        for(int i = 0; i < buff.length - (buff.length % 2); i += 2){
            byte r = buff[i];
            buff[i] = (byte)(buff[i+1] + keyb[i % yor]);
            buff[i+1] = (byte)(r + keyb[(i + 1) % yor]);
        }
        return buff;
    }
    private static String decoding(byte[] buff, String key){
        byte[] keyb = key.getBytes();
        int yor = keyb.length;
        int ik = 0;
        //byte[] buff = str.getBytes();
        for(int i = 0; i < buff.length - (buff.length % 2); i += 2){
            byte r = buff[i];
            buff[i] = (byte)(buff[i+1] - keyb[(i + 1) % yor]);
            buff[i+1] = (byte)(r - keyb[(i) % yor]);
        }
        return new String(buff, StandardCharsets.UTF_8);
    }
    private static void writeInFile(byte[] b, OutputStream stream){
        try{
            stream.write(b.length);
            for (int i = 0; i < b.length; i++)
                stream.write(b[i]);
        }catch (IOException ex){}
    }
    private class KeyPref{
        String key;
        String prefix;
        KeyPref(String a, String b){
            key = a;
            prefix = b;
        }
    }
}
