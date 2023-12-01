package mindustry.server;

import arc.util.ColorCodes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class LogManager {
    public LogManager() {
    }

    public static void logToFile(String text, ServerControl serverControl) {
        DateTimeFormatter date = ServerControl.dateTime;
        String thisDate = date.format(LocalDateTime.now()).split(" ")[0];
        if (serverControl.currentLogFile != null && !("log-" + thisDate + ".txt").equals(serverControl.currentLogFile.name())) {
            DateTimeFormatter var10001 = ServerControl.dateTime;
            serverControl.currentLogFile.writeString("[End of log file. Date: " + var10001.format(LocalDateTime.now()) + "]\n", true);
            serverControl.currentLogFile = null;
        }

        String[] var3 = ColorCodes.values;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String value = var3[var5];
            text = text.replace(value, "");
        }

        if (serverControl.currentLogFile == null) {
            serverControl.currentLogFile = serverControl.logFolder.child("log-" + thisDate + ".txt");
        }

        serverControl.currentLogFile.writeString(text + "\n", true);
    }

    public static String translate(String text) {
        String var10000;
        switch (text) {
            case "Game over! Reached wave @ with @ players online on map @.":
                var10000 = "Игра окончена! Достигнута волна @ с @ игроками онлайн на карте @.";
                break;
            case "Game over! Team @ is victorious with @ players online on map @.":
                var10000 = "Игра окончена! Команда @ одерживает победу с @ игроками онлайн на карте @.";
                break;
            case "Selected next map to be @.":
                var10000 = "Выбранная карта: @.";
                break;
            case "Auto-save loaded.":
                var10000 = "Автосохранение загружено.";
                break;
            case "Found @ command-line arguments to parse.":
                var10000 = "Найдено @ аргументов терминала для парсинга.";
                break;
            case "Found @ startup commands.":
                var10000 = "Найдено @ команд запуска.";
                break;
            case "Autosaving...":
                var10000 = "Автосохранение...";
                break;
            case "Autosave completed.":
                var10000 = "Автосохранение завершено.";
                break;
            case "@ mods loaded.":
                var10000 = "Загружено модификаций: @.";
                break;
            case "Server loaded. Type @ for help.":
                var10000 = "Сервер загружен. Используйте @ для получения помощи.";
                break;
            case "Commands:":
                var10000 = "Команды:";
                break;
            case "Version: Mindustry @-@ @ / build @":
                var10000 = "Версия: Mindustry @-@ @ / build @";
                break;
            case "Java Version: @":
                var10000 = "Версия Java: @";
                break;
            case "Shutting down server.":
                var10000 = "Отключение сервера.";
                break;
            case "Stopped server.":
                var10000 = "Сервер остановлен.";
                break;
            case "Randomized next map to be @.":
                var10000 = "Следующая случайная карта: @.";
                break;
            case "Loading map...":
                var10000 = "Загрузка карты...";
                break;
            case "Map loaded.":
                var10000 = "Карта загружена.";
                break;
            case "No custom maps loaded. &fiTo display built-in maps, use the \"@\" argument.":
                var10000 = "Нет пользовательских карт. &fiЧтобы показать встроенные карты, используйте аргумент \"@\".";
                break;
            case "Maps:":
                var10000 = "Карты:";
                break;
            case "  @ (@): &fiCustom / @x@":
                var10000 = "  @: &fiПользовательские / @x@";
                break;
            case "  @: &fiDefault / @x@":
                var10000 = "  @: &fiВстроенные / @x@";
                break;
            case "No maps found.":
                var10000 = "Карты не найдены";
                break;
            case "Map directory: &fi@":
                var10000 = "Каталог карт: &fi@";
                break;
            case "@ new map(s) found and reloaded.":
                var10000 = "Новых карт найдено и загружено: @.";
                break;
            case "@ old map(s) deleted.":
                var10000 = "Старых карт удалено: @.";
                break;
            case "Maps reloaded.":
                var10000 = "Карты перезагружены.";
                break;
            case "Status: &rserver closed":
                var10000 = "Статус: &rсервер закрыт.";
                break;
            case "Status:":
                var10000 = "Статус:";
                break;
            case "  Playing on map &fi@ / Wave @":
                var10000 = "  Игра на карте &fi@ / Волна @";
                break;
            case "  @ seconds until next wave.":
                var10000 = "  @ секунд до следующей волны.";
                break;
            case "  @ units / @ enemies":
                var10000 = "  @ юниты / @ враги";
                break;
            case "  @ FPS, @ MB used.":
                var10000 = "  @ FPS, @ MB использовано.";
                break;
            case "  Players: @":
                var10000 = "  Игроки: @";
                break;
            case "  No players connected.":
                var10000 = "  Нет игроков.";
                break;
            case "Mods:":
                var10000 = "Модификации:";
                break;
            case "No mods found.":
                var10000 = "Модификации не найдены.";
                break;
            case "Mod directory: &fi@":
                var10000 = "Каталог модификаций: &fi@";
                break;
            case "Name: @":
                var10000 = "Название: @";
                break;
            case "Internal Name: @":
                var10000 = "Внутреннее название: @";
                break;
            case "Version: @":
                var10000 = "Версия: @";
                break;
            case "Author: @":
                var10000 = "Автор: @";
                break;
            case "Path: @":
                var10000 = "Путь: @";
                break;
            case "Description: @":
                var10000 = "Описание: @";
                break;
            case "No mod with name '@' found.":
                var10000 = "Модификация с названием '@' не найдена.";
                break;
            case "Game paused.":
                var10000 = "Игра приостановлена.";
                break;
            case "Game unpaused.":
                var10000 = "Игра вознобновлена.";
                break;
            case "Rules:\n@":
                var10000 = "Правила:\n@";
                break;
            case "Rule '@' removed.":
                var10000 = "Правило '@' удалено.";
                break;
            case "Changed rule: @":
                var10000 = "Изменено правило: @";
                break;
            case "Core filled.":
                var10000 = "Ядро заполнено.";
                break;
            case "Player limit is currently @.":
                var10000 = "Лимит игроков равен @.";
                break;
            case "Player limit disabled.":
                var10000 = "Ограничение на количество игроков снято.";
                break;
            case "Player limit is now &lc@":
                var10000 = "Лимит игроков теперь: @.";
                break;
            case "All config values:":
                var10000 = "Все значения настроек:";
                break;
            case "'@' is currently @.":
                var10000 = "'@' сейчас @.";
                break;
            case "@ set to @":
                var10000 = "@ изменено на @";
                break;
            case "Subnets banned: @":
                var10000 = "Заблокированные подсети: @";
                break;
            case "Banned @**":
                var10000 = "Заблокирован @**";
                break;
            case "Unbanned @**":
                var10000 = "Разлокирован @**";
                break;
            case "No whitelisted players found.":
                var10000 = "Нет игроков в белом списке.";
                break;
            case "Whitelist:":
                var10000 = "Белый список:";
                break;
            case "- Name: @ / UUID: @":
                var10000 = "- Ник: @ / UUID: @";
                break;
            case "Player '@' has been whitelisted.":
                var10000 = "Игрок '@' добавлен в белый список.";
                break;
            case "Player '@' has been un-whitelisted.":
                var10000 = "Игрок '@' удалён из белого списка.";
                break;
            case "Shuffle mode current set to '@'.":
                var10000 = "Режим перемешивания установлен на '@'.";
                break;
            case "Shuffle mode set to '@'.":
                var10000 = "Режим перемешивания изменён на '@'.";
                break;
            case "Next map set to '@'.":
                var10000 = "Следующая карта: '@'.";
                break;
            case "It is done.":
                var10000 = "Успешно.";
                break;
            case "Nobody with that name could be found...":
                var10000 = "Нет здесь таких.";
                break;
            case "Banned.":
                var10000 = "БАН!!!";
                break;
            case "No ID-banned players have been found.":
                var10000 = "Нет игроков, заблокированных по ID.";
                break;
            case "Banned players [ID]:":
                var10000 = "Заблокированные игроки [ID]:";
                break;
            case " @ / Last known name: '@'":
                var10000 = " @ / Последний известный ник: '@'";
                break;
            case "  '@' / Last known name: '@' / ID: '@'":
                var10000 = "  '@' / Последний известный ник: '@' / ID: '@'";
                break;
            case "  '@' (No known name or info)":
                var10000 = "  '@' (Нет информации)";
                break;
            case "Unbanned player: @":
                var10000 = "Разблокированный игрок: @";
                break;
            case "Pardoned player: @":
                var10000 = "Помилованный игрок: @";
                break;
            case "Changed admin status of player: @":
                var10000 = "Изменён админ. статус игрока @.";
                break;
            case "No admins have been found.":
                var10000 = "Анархия и саморегуляция! Администраторов не найдено.";
                break;
            case "Admins:":
                var10000 = "Администраторы:";
                break;
            case "No players are currently in the server.":
                var10000 = "Нет игроков на сервере.";
                break;
            case "Wave spawned.":
                var10000 = "Волна запущена.";
                break;
            case "Save loaded.":
                var10000 = "Сохранение загружено.";
                break;
            case "Saved to @.":
                var10000 = "Сохранено в: @.";
                break;
            case "Save files: ":
                var10000 = "Сохранить файлы:";
                break;
            case "Core destroyed.":
                var10000 = "Ядро уничтожено.";
                break;
            case "Players found: @":
                var10000 = "Игроки найдены: @";
                break;
            case "[@] Trace info for player '@' / UUID @ / RAW @":
                var10000 = "[@] Информация о трассировке для игрока '@' / UUID @ / RAW @";
                break;
            case "  all names used: @":
                var10000 = "  все используемые ники: @";
                break;
            case "  all IPs used: @":
                var10000 = "  все используемые IP: @";
                break;
            case "  times joined: @":
                var10000 = "  время присоединения: @";
                break;
            case "  times kicked: @":
                var10000 = "  его пнули @ раз";
                break;
            case "Nobody with that name could be found.":
                var10000 = "Таких здесь не водится.";
                break;
            case "@ MB collected. Memory usage now at @ MB.":
                var10000 = "@ MB собрано. Теперь используется @ MB.";
                break;
            case "&lkReceived command socket connection: &fi@":
                var10000 = "&lkПолучена команда на соединение с сокетом: &fi@";
                break;
            default:
                var10000 = text;
        }

        return var10000;
    }
}
