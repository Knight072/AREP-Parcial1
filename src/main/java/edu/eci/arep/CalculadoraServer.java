package edu.eci.arep;

/**
 * Hello world!
 *
 */
public class CalculadoraServer
{
    public static void main( String[] args )
    {
        HttpServer.getInstance().setStaticFiles("target/classes/public");
        load();
        try{
            HttpServer.getInstance().start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void load(){
        PathServices.getInstance();
    }
}
