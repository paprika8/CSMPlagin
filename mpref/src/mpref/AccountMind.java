package mpref;

import arc.util.Strings;
import mindustry.gen.Player;

import java.util.ArrayList;

public class AccountMind {
    Player player;
    public static final String[] prefixes = new String[]{"[green][U][]", "[cyan][Mл M][]", "[sky][M][]", "[blue][Cт M][]", "[red][A][]", "[SCARLET][Ст A][]", "[BRICK][Кур A][]", "[TAN][ЗГА][]", "[GOLDENROD][ОЗГА][]", "[GOLD][ГA][]", "[ORANGE][зСA][]", "[MAROON][СA][]", "[OLIVE][КП][]", "[red][Т/А][]", "[red][Ст Т/А][]", "[SCARLET][Кур Т/А][]", "[SCARLET][ГКур Т/А][]", "[BRICK][зГ Т/С][]", "[CRIMSON][Г Т/С][]", "[red][Ютубер][]","[yellow][VIP][]"};
    String prefix = "[green][U][]";
    final String name;
    int level = 0;
    static ArrayList<AccountMind> all = new ArrayList<>();
    AccountMind(Player pl){
        player = pl;
        name = pl.name;
        player.name = "" + prefix + " " + name;
        all.add(this);
    }
    public void setPrefix(String prefix) {
        switch (prefix) {
            case "[U]":
                this.prefix = prefixes[0];
                level = 0;
                break;
            case "[Мл_М]":
                this.prefix = prefixes[1];
                break;
            case "[М]":
                this.prefix = prefixes[2];
                break;
            case "[Ст_М]":
                this.prefix = prefixes[3];
                break;
            case "[А]":
                this.prefix = prefixes[4];
                break;
            case "[Ст_А]":
                this.prefix = prefixes[5];
                break;
            case "[Кур_А]":
                this.prefix = prefixes[6];
                break;
            case "[ЗГА]":
                this.prefix = prefixes[7];
                break;
            case "[ОЗГА]":
                this.prefix = prefixes[8];
                break;
            case "[ГА]":
                this.prefix = prefixes[9];
                break;
            case "[зСА]":
                this.prefix = prefixes[10];
                break;
            case "[СА]":
                this.prefix = prefixes[11];
                break;
            case "[КП]":
                this.prefix = prefixes[12];
                break;
            case "[Т/А]":
                this.prefix = prefixes[13];
                break;
            case "[Ст_Т/А]":
                this.prefix = prefixes[14];
                break;
            case "[Кур_Т/А]":
                this.prefix = prefixes[15];
                break;
            case "[ГКур_Т/А]":
                this.prefix = prefixes[16];
                break;
            case "[зГ_Т/С]":
                this.prefix = prefixes[17];
                break;
            case "[Г_Т/С]":
                this.prefix = prefixes[18];
                break;
            case "[Ют]":
                this.prefix = prefixes[19];
                break;
            case "[VIP]":
                this.prefix = prefixes[20];
                break;
        }
        player.name = "" + this.prefix + " " + name;
    }

    public void setPrefix(int i) {
        this.prefix = prefixes[i];
        player.name = "" + prefix + " " + name;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public int getLevel() {
        return this.level;
    }
    public static AccountMind ofPlayer(Player pl){
        for (AccountMind account: all) {
            if(account.player.uuid().equals(pl.uuid()))
                return account;
        }
        return null;
    }
}
