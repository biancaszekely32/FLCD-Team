//public class Pair <T1, T2>{
//
//    private T1 first;
//    private T2 second;
//
//    public Pair(T1 first, T2 second){
//        this.first = first;
//        this.second = second;
//    }
//
//    public T1 getFirst(){
//        return first;
//    }
//
//    public T2 getSecond(){
//        return second;
//    }
//
//    @Override
//    public boolean equals(Object obj){
//        if(obj == null || !(obj instanceof Pair)){
//            return false;
//        }
//        Pair other = (Pair) obj;
//        return first.equals(other.first) && second.equals(other.second);
//    }
//
//    @Override
//    public int hashCode(){
//        return first.hashCode() + second.hashCode();
//    }
//
//    @Override
//    public String toString(){
//        return "(" + first + ", " + second + ")";
//    }
//}