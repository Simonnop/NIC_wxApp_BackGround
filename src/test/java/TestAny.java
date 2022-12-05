import group.pojo.util.MyTime;
import org.junit.Test;

public class TestAny {
    @Test
    public void test(){
        String a = null;
        System.out.println(a);
        int b = 0;
        System.out.println(b);

        MyTime myTime = new MyTime();
        System.out.println(myTime.getTime());
    }
}
