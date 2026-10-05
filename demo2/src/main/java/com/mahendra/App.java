package com.mahendra;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try (JavaConfigApplicationContext context =
                     new JavaConfigApplicationContext(AppConfig.class)) {
                        
            Greeting greeter = context.getBean(Greeting.class);
            greeter.sayHello("World");

            Greeting greeter2 = context.getBean(Greeting.class);
            greeter2.sayHello("Mahendra");
        }
    }
}
