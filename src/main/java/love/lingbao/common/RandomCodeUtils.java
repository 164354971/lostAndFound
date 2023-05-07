package love.lingbao.common;

public class RandomCodeUtils {
    public static String getSixValidationCode(){
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 6; i++) {
            int num = (int) (Math.random() * 10);
            sb.append(num);
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
