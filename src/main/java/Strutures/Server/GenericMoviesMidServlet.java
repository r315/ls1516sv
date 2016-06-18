package Strutures.Server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Red on 28/05/2016.
 */
/*
class TreeNode{
    String value;
    TreeNode right;
    TreeNode left;

    public TreeNode(String v){
        this.value=v;
    }

    public TreeNode(String v, TreeNode r, TreeNode l){
        this.value=v;
        this.right=r;
        this.left=l;
    }

    public void addLeft(TreeNode l){
        left=l;
    }

    public void addRight(TreeNode r){
        right=r;
    }

    private static TreeNode CreateTree(){
        TreeNode root = new TreeNode("movies");
        root.addLeft(new TreeNode("ratings"));
        //TreeNode
        return root;
    }

}
*/

public class GenericMoviesMidServlet extends HttpServlet {

    //String[] keywords={"ratings", "reviews",""}
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] a= req.getPathInfo().split("/");
        //1/reviews/1
        //1/ratings
        //1
        //1/reviews
        switch(a.length){
            case 2:
                new MoviesMidServlet().doGet(req, resp);
                break;
            case 3:
                if (a[2].equals("ratings"))
                    new MoviesMidRatingsServlet().doGet(req, resp);
                else if(a[2].equals("reviews"))
                    new MoviesMidReviewsServlet().doGet(req, resp);
                else ErrorPage(resp);
                break;
            case 4:
                if(a[2].equals("reviews"))
                    new MoviesMidReviewsRidServlet().doGet(req,resp);
                break;
            default:
                ErrorPage(resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] a= req.getPathInfo().split("/");

        if(a.length!=3)
            ErrorPage(resp);

        switch(a[2]){
            case "ratings":
                new MoviesMidRatingsServlet().doPost(req, resp);
                break;
            case "reviews":
                new MoviesMidReviewsServlet().doPost(req, resp);
                break;
            default:
                ErrorPage(resp);
        }
    }

    private static void ErrorPage(HttpServletResponse resp)throws IOException{
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(404);
        String respBody="Error 404.";
        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
