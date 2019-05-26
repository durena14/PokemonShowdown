import java.util.*;
public class Battle{
    private int currentUserIndex,currentOppIndex;
    private Pokemon[] player;
    private Pokemon[] AI;
    private Pokemon currentUser;
    private Pokemon currentOpp;
    
    public Battle(Pokemon[] p1,Pokemon[] p2){
        player=new Pokemon[6];
        AI=new Pokemon[6];
        for(int i=0;i<6;i++){
            player[i]=p1[i];
            AI[i]=p2[i];
        }
        currentUser=player[0];
        currentUserIndex=0;
        currentOpp=AI[0];
        currentOppIndex=0;
    }
    
    //Ethan Kim
    private int damageCalc(Move userMove, Pokemon user, Pokemon opp){
        double random=Math.random();
        int critical=1;
        if(random<0.10)
            critical=2;
        float modifier=userMove.getEffectiveness(opp)*userMove.stab(user)*critical;
        return (int) (((((2*user.getLevel())/5+2) * userMove.getPower() * (user.getAttack()/opp.getDefense()))/50 + 2) * modifier);
    }
    
    public void hit(Move userMove, Pokemon user, Pokemon opp){
        double random=Math.random();
        if(random<=userMove.getAccuracy()){
            opp.takeDamage(damageCalc(userMove,user,opp));
            if(userMove.getName().equals("Struggle")){
                user.takeDamage(damageCalc(userMove,user,opp)/2);
            }
            userMove.useMove();
            user.outOfPP();
        }else{
            //dialogue box (you missed)
        }
    }
    
    public Pokemon getCurrentUser(){
        return currentUser;
    }
    
    public Pokemon getCurrentOpponent(){
        return currentOpp;
    }
    
    public void switchPokemon(int index){
        if(!player[index].isDead()){
            currentUser=player[index];
            currentUserIndex=index;
            refreshUI();
        }
    }
    
    private int typeAdv(Pokemon user, Pokemon opp){
        Move oppTest=new Move("test",10,1.0f,opp.getType().get(0),1);
        Move oppTest2=null;
        if(!opp.getType().get(1).equals("")){
            oppTest2=new Move("test2",10,1.0f,opp.getType().get(1),1);
        }
        
        int advantage=-1*moveAdv(oppTest,user);
        if(oppTest2!=null){
            advantage+=(-1*moveAdv(oppTest2,user));
        }
        
        advantage+=moveAdv(user.getMove(maxDamageIndex(user,opp)),opp);
        
        return advantage;
    }
    
    private static int moveAdv(Move userMove, Pokemon opp){
        if(Math.abs(userMove.getEffectiveness(opp)-4)<0.1){
            return 2;
        }else if(Math.abs(userMove.getEffectiveness(opp)-2)<0.1){
            return 1;
        }else if(Math.abs(userMove.getEffectiveness(opp)-1)<0.1){
            return 0;
        }else if(Math.abs(userMove.getEffectiveness(opp)-0.5)<0.1){
            return -1;
        }else if(Math.abs(userMove.getEffectiveness(opp)-0.25)<0.1){
            return -2;
        }else{
            return -3;
        }
    }
    
    private int maxDamageIndex(Pokemon user, Pokemon opp){
        int maxDamage=0;
        int moveIndex=0;
        
        for(int i=0;i<user.getMoveset().length;i++){
            if(user.getMove(i).getPP()>0){
                moveIndex=i;
                maxDamage=damageCalc(user.getMove(i),user,opp);
                break;
            }
        }
        
        for(int i=moveIndex+1;i<user.getMoveset().length;i++){
            if(user.getMove(i).getPP()>0){
                if(damageCalc(user.getMove(i),user,opp)>maxDamage){
                    moveIndex=i;
                    maxDamage=damageCalc(user.getMove(i),user,opp);
                }
            }
        }
        
        return moveIndex;
    }
   
    
    private int advantage(Pokemon user, Pokemon opp){
        return typeAdv(user,opp)+HPAdv(user)+speedAdv(user,opp)-HPAdv(opp);
    }
    
    private boolean hitOrSwap(double chances){
        double random=Math.random();
        if(random<=chances){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean AITurn(){
        int adv=advantage(currentOpp,currentUser);
        if(adv>0){
            return hitOrSwap(1.0);
        }else if(adv<=-9){
            return hitOrSwap(-1.0);
        }else if(adv<=-6){
            return hitOrSwap(0.05);
        }else if(adv<=-3){
            return hitOrSwap(0.1);
        }else if(adv<0){
            return hitOrSwap(0.2);
        }else{
            return hitOrSwap(0.90);
        }
    }
    
    private static int speedAdv(Pokemon user, Pokemon opp){
        if(user.getSpeed()>opp.getSpeed()){
            return 1;
        }else if(user.getSpeed()==opp.getSpeed()){
            return 0;
        }else{
            return -1;
        }
    }
    
    private static int HPAdv(Pokemon user){
        if(user.getCurrentHP()/user.getBaseHP()==1){
            return 2;
        }else if(1.0*user.getCurrentHP()/user.getBaseHP()>=0.75){
            return 1;
        }else if(1.0*user.getCurrentHP()/user.getBaseHP()>=0.5){
            return 0;
        }else if(1.0*user.getCurrentHP()/user.getBaseHP()>=0.25){
            return -1;
        }else{
            return -2;
        }
    }
    
    private void AISwitch(){
        int maxAdvantage=advantage(currentOpp,currentUser);
        for(int i=0;i<AI.length;i++){
            if(!AI[i].isDead()){
                if(advantage(AI[i],currentUser)>maxAdvantage){
                    maxAdvantage=advantage(AI[i],currentUser);
                    currentOppIndex=i;
                    currentOpp=AI[currentOppIndex];
                }
            }
        }
        refreshUI();
    }
    
    public void turn(){
        if(!AITurn()){
            
        }
    }
}