package com.mahendra;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Logger log = new Logger();
        SPLogger slog = new SPLogger(log);
        
        Greeting greeter = new Greeting(slog);
        greeter.sayHello("World");
        
        Greeting greeter2 = new Greeting(slog);
        greeter2.sayHello("Mahendra");
    }
}
