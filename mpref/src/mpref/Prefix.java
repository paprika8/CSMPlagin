package mpref;
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

public class Prefix extends Plugin {
    private HashSet<String> votes = new HashSet();
    File filePass, fileID;
    List<KeyPref> UUIDs = new ArrayList<>();
    //final BanHelper helper = new BanHelper();
// xyzdiuP geue
    public Prefix() {
        Events.on(EventType.PlayerLeave.class, (e) -> {
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
                e.player.admin(
                        !(AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[0]) || AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[20]))
                );
                Log.info(e.player.uuid() + " зашёл как " + (((AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[0]) || AccountMind.ofPlayer(e.player).getPrefix().equals(AccountMind.prefixes[20]))
                )? "пользователь" : "админ"));
            }
            else {
                Log.err("Account", "Account is lost");
            }

        });
        Events.on(EventType.GameOverEvent.class, (e) -> {
            this.votes.clear();
        });
        /*try {
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
        }*/
        try {
            Path path;
            Path buff = Paths.get("/home/container");
            if (!Files.exists(buff))
                path = Files.createDirectory(buff);
            else path = buff;

            buff = Paths.get("/home/container/config");
            if (!Files.exists(buff))
                path = Files.createDirectory(buff);
            else path = buff;
            buff = Paths.get("/home/container/config/passwords");
            if (!Files.exists(buff))
                path = Files.createDirectory(buff);
            else path = buff;

            buff = Paths.get("/home/container/config/passwords/pass.b");
            if (!Files.exists(buff))
                path = Files.createFile(buff);
            else path = buff;
            filePass = new File(path.toUri());
            buff = Paths.get("/home/container/config/passwords/id.b");
            if (!Files.exists(buff))
                path = Files.createFile(buff);
            else path = buff;
            fileID = new File(path.toUri());
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream stream = Files.newInputStream(Paths.get("/home/container/config/passwords/id.b"));
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
    public void registerClientCommands(CommandHandler handler) {
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
                    player.admin(
                            !(AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[0]) || AccountMind.ofPlayer(player).getPrefix().equals(AccountMind.prefixes[20]))
                    );
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

        /*handler.<Player>register("mban", "[uuid]", "ban", (args, player) ->{
            if(player != null  && args.length != 0 && !args[0].isEmpty())
                helper.ban(args[0], player);
        });
        handler.<Player>register("muban", "[uuid]", "uban", (args, player) ->{
            if(player != null  && args.length != 0 && !args[0].isEmpty())
                helper.uban(args[0], player);
        });*/
    }
    private List<KeyPref> getAllPass(String key){
        List<KeyPref> Passs = new ArrayList<>();
        try{
            InputStream stream = Files.newInputStream(filePass.toPath());
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
