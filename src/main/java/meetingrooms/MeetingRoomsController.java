package meetingrooms;

import java.util.Scanner;

public class MeetingRoomsController {

    private MeetingRoomsService meetingRoomsService;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MeetingRoomsController controller = new MeetingRoomsController();
        controller.choiceDataBaseType();
        controller.recordMeetingRooms();

        controller.printMenu();
        controller.runMenu();

        controller.deleteAll();
    }

    private void deleteAll() {
        meetingRoomsService.deleteAll();
    }

    private static void accept(MeetingRoom e) {
        System.out.println(e.getAll());
    }

    private void runMenu() {
        System.out.println("Kérem válaszon menüpontot");

        int menuNumber = scanner.nextInt();
        scanner.nextLine();

        callMenu(menuNumber);
    }

    private void callMenu(int menuNumber) {
        switch (menuNumber) {

            case 1 -> printOrderedNames();

            case 2 -> printReversedNames();

            case 3 -> printEvenOrderdNames();

            case 4 -> printMeetingRoomsOrderedByAreaDesc();

            case 5 -> printDimensionsByName();

            case 6 -> printDimensionsByPartOfName();

            case 7 -> printAreasLargerThan();
        }
    }

    private void printAreasLargerThan() {
        System.out.println("Hány m2-nél nagyobb tárgyalókra kíváncsi?");
        int area = scanner.nextInt();
        scanner.nextLine();

        meetingRoomsService.getAreasLargerThan(area).forEach(e -> System.out.println(e.getAll()));
    }

    private void printDimensionsByPartOfName() {
        System.out.println("Milyen névtöredékre keressek?");
        String part = scanner.nextLine();

        meetingRoomsService.getMeetingRoomsContains(part).forEach(e -> System.out.println(e.getDimensions()));
    }

    private void printDimensionsByName() {
        System.out.println("Milyen névre keressek?");
        String name = scanner.nextLine();

        System.out.println(meetingRoomsService.getMeetingRoomsWithName(name).isPresent() ? meetingRoomsService.getMeetingRoomsWithName(name).get().getDimensions() : "Nincs ilyen nevű tárgyaló");
    }

    private void printMeetingRoomsOrderedByAreaDesc() {
        meetingRoomsService.getMeetingRoomsOrderedByAreaDesc().forEach(e -> System.out.println(e.getAll()));
    }

    private void printEvenOrderdNames() {
        meetingRoomsService.getEvenOrderedNames().forEach(System.out::println);
    }

    private void printReversedNames() {
        meetingRoomsService.getReversedNames().forEach(System.out::println);
    }

    private void printOrderedNames() {
        meetingRoomsService.getOrderedNames().forEach(System.out::println);
    }

    private void printMenu() {
        System.out.println("\nVálasztható menüpontok:");
        System.out.println("1. Tárgyalók sorrendben");
        System.out.println("2. Tárgyalók visszafele sorrendben");
        System.out.println("3. Minden második tárgyaló");
        System.out.println("4. Területek");
        System.out.println("5. Keresés pontos név alapján");
        System.out.println("6. Keresés névtöredék alapján");
        System.out.println("7. Keresés terület alapján");
    }

    private void recordMeetingRooms() {
        System.out.println("Hány tárgyalót szeretne rögzíteni?");
        int meetingRoomsNumber = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= meetingRoomsNumber; i++) {

            System.out.println("Kérem a(z) " + i + ". tárgyaló nevét.");
            String name = scanner.nextLine();

            System.out.println("Kérem a szélességét.");
            int width = scanner.nextInt();

            System.out.println("Kérem a hosszát.");
            int length = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            meetingRoomsService.save(name, width, length);
        }
    }

    private void choiceDataBaseType() {
        System.out.println(
                "Milyen adatfeldolgozást szeretne:\n" +
                        "1. Adatbázis alapú\n" +
                        "2. Memória alapú");

        int num = scanner.nextInt();
        scanner.nextLine();

        initializonMeetingRoomsService(num);
    }

    private void initializonMeetingRoomsService(int num) {
        switch (num) {
            case 1 -> meetingRoomsService = new MeetingRoomsService(new MySqlMeetingRoomsRepository());

            case 2 -> meetingRoomsService = new MeetingRoomsService(new InMemoryMeetingRoomsRepository());

            default -> choiceDataBaseType();
        }
    }
}
