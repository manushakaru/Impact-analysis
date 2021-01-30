public class ATest {
    private String name;

    public A(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setdsdName(String name) {
        this.name = name;
        System.out.println(this.name);
        getName();
        getName();
    }


    
    public void changeName(){
        this.name = name+"---";
    }

    public static void main(String[] args) {
        B b = new B();
    }
    
}
