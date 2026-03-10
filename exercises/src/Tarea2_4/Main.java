package Tarea2_4;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();

        System.out.println("--- LIST OF OWNERS ---");
        ArrayList<Owner> owners = (ArrayList<Owner>) db.getOwners();
        for (Owner o : owners) {
            System.out.println(o);
        }

        System.out.println("\n--- LIST OF CATS ---");
        ArrayList<Animal> cats = (ArrayList<Animal>) db.getCats();
        for (Animal c : cats) {
            System.out.println(c);
        }

        System.out.println("\n--- ADDING NEW ANIMAL ---");
        Animal newAnimal = new Animal(200, "Bigotes", "gato", "12345678A");
        db.addAnimal(newAnimal);

        System.out.println("\n--- DELETING OWNER ---");
        db.deleteOwner("11111111B");

        System.out.println("\n--- ADDING OWNER WITH AUDIT ---");
        Owner newOwner = new Owner("99999999Z", "Test User");
        db.addOwnerWithAudit(newOwner);

        System.out.println("\n--- END OF EXECUTION ---");
    }
}