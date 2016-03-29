package console;

interface ICommand {
    //PreparedStatement Command(List<Object> args, Connection conn);
    void execute();
}
public class Test {

    public static void main(String[]args){
        ICommand c= () -> System.out.println(50);
        
        c.execute();
    }
}