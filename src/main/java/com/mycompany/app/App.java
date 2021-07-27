package com.mycompany.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );
        ApiCalls api = new ApiCalls();
        api.fetchNews();
    }
}
