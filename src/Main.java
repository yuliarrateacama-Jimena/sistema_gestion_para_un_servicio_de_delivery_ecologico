import interfaz.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        try {
            MenuPrincipal menu = new MenuPrincipal();
            menu.iniciar();
        } catch (Exception e) {
            System.err.println("\n❌ ERROR CRÍTICO EN EL SISTEMA ❌");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
