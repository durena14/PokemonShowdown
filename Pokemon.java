//Diego Urena
import java.util.ArrayList;
public class Pokemon{
    private String name, type1,type2;
    private int level,hpo,hpc,attack,defense,speed;
    private Move[] moveset;
    private int pokedex;
    private boolean alive;
    
    public Pokemon(String name, int hp,int attack, int defense, int speed, String[] types,Move[] moves,int pokedex){
        this.name=name;
        type1=types[0];
        type2=(types[1]==null)?"":types[1];
        level=(int) (Math.random()*11)+79;
        hpo=effectiveHP(hp);
        hpc=effectiveHP(hp);
        this.attack=effectiveStat(attack);
        this.defense=effectiveStat(defense);
        this.speed=effectiveStat(speed);
        moveset=new Move[moves.length];
        for(int i=0;i<moveset.length;i++){
            moveset[i]=moves[i];
        }
        this.pokedex=pokedex;
        alive=true;
    }
    
    public String getName(){
        return name;
    }
    
    public ArrayList<String> getType(){
        ArrayList<String> types=new ArrayList<String>();
        types.add(type1);
        types.add(type2);
        return types;
    }
    
    public int getLevel(){
        return level;
    }
    
    public void takeDamage(int damage){
        hpc=(damage>=hpc)?0:hpc-damage;
        alive=(hpc==0)?false:true;
    }
    
    public boolean isDead(){
        return !alive;
    }
    
    public Move[] getMoveset(){
        return moveset;
    }
    
    public Move getMove(int index){
        return moveset[index];
    }
    
    public int getPokedex(){
        return pokedex;
    }
    
    public int getBaseHP(){
        return hpo;
    }
    
    public int getCurrentHP(){
        return hpc;
    }
    
    public int getAttack(){
        return attack;
    }
    
    public int getDefense(){
        return defense;
    }
    
    public int getSpeed(){
        return speed;
    }
    
    private int effectiveHP(int base){
        return (int)((((((base+31)*2)+(Math.sqrt(127.5)/4))*level)/100)+level+10);
    }
    
    private int effectiveStat(int base){
        return (int)((((((base+31)*2)+(Math.sqrt(127.5)/4))*level)/100)+5);
    }
    
    public void outOfPP(){
        boolean moves=false;
        for(int i=0;i<moveset.length;i++){
            if(moveset[i].getPP()>0){
                moves=true;
            }
        }
        
        if(!moves){
            moveset=new Move[1];
            moveset[0]=new Move("Struggle",35,0.95f,"normal",10000);
        }
    }
}
