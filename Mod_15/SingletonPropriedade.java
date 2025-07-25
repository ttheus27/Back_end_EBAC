package Mod_15;

public class SingletonPropriedade {
    private static SingletonPropriedade singleton;
    private String value;


    private SingletonPropriedade(String value){
        this.value = value;
    }

    public static SingletonPropriedade getSingleton(String value){
        if (singleton ==null){
            singleton = new SingletonPropriedade(value);

        }
        return singleton;
    }
}
