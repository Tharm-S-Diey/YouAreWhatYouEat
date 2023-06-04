
/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Num
{
    // instance variables - replace the example below with your own
    public int x;

    public Num(int d) {
        x = d;
    }
    
    public static void main(String[] args) {
        Num first = new Num(3);
        Num second = new Num(8);
        first = second;
        System.out.println(first.x);
    }
}
