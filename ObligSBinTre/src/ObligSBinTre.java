////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{



  public static void main (String[] args){
   // Integer[] a = {4,7,2,9,4,10,8,7,4,6};
  //  Integer[] a={10,5,4,2,1,3,8,9,7,15,13,11,14,17,18,16,19};


      ObligSBinTre<Integer> tre =new ObligSBinTre<>(Comparator.naturalOrder());
      // TODO: feil på 5
      int[] b = {5, 4, 3, 2, 1};
      for (int k : b) tre.leggInn(k);
      int k = 4;
     // tre.fjern(k);
      //System.out.println("");

System.out.println(tre.toString());
 System.out.println(tre.omvendtString());


    //.out.println("Verdi"+tre.rot.venstre);
    //System.out.println("Neste inorden"+tre.nesteInorden(tre.rot.venstre).verdi);
  /*  System.out.println("Verdi"+tre.rot.venstre.venstre.venstre.høyre.verdi);
    System.out.println("Neste inorden"+tre.nesteInorden(tre.rot.venstre.venstre.venstre.høyre).verdi);

    System.out.println("Verdi"+tre.rot.venstre.høyre);
    System.out.println("Neste inorden"+tre.nesteInorden(tre.rot.venstre.høyre).verdi);

    System.out.println("Verdi"+tre.rot.høyre.høyre);*/
   // System.out.println("Neste inorden"+tre.nesteInorden(tre.rot.høyre.høyre).verdi);


  }



  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og høyre barn
    private Node<T> forelder;          // forelder

    // konstruktør
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktør
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }

  //////////////////////////// OPPGAVE 1 //////////////////////////////////
  @Override
  public final boolean leggInn(T verdi)    // skal ligge i class SBinTre
  {
    Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

    Node<T> p = rot, q = null;               // p starter i roten
    int cmp = 0;                             // hjelpevariabel

    while (p != null)       // fortsetter til p er ute av treet
    {
      q = p;                                 // q er forelder til p
      cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
      p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
    }

    // p er nå null, dvs. ute av treet, q er den siste vi passerte

    p = new Node<T>(verdi, q);                   // oppretter en ny node

    if (q == null) rot = p;                  // p blir rotnode
    else if (cmp < 0) q.venstre = p;         // venstre barn til q
    else q.høyre = p;                        // høyre barn til q

    antall++;                                // én verdi mer i treet
     // System.out.println("verdi"+verdi+"lagt inn");
    return true;                             // vellykket innlegging
  }
  
  @Override
  public boolean inneholder(T verdi)
  {
    if (verdi == null) return false;

    Node<T> p = rot;

    while (p != null)
    {
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp < 0) p = p.venstre;
      else if (cmp > 0) p = p.høyre;
      else return true;
    }

    return false;
  }

  ////////////////////////////// OPPGAVE 5 ////////////////////////////////
  @Override
  public boolean fjern(T verdi)
  {

      if (verdi == null) return false;  // treet har ingen nullverdier

      Node<T> p = rot, forelder = null;   // forelder skal være forelder til p

      while (p != null)            // leter etter verdi
      {
          int cmp = comp.compare(verdi,p.verdi);      // sammenligner
          if (cmp < 0) { forelder = p; p = p.venstre; }      // går til venstre
          else if (cmp > 0) { forelder = p; p = p.høyre; }   // går til høyre
          else break;    // den søkte verdien ligger i p
      }
      if (p == null) return false;   // finner ikke verdi

      if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
      {
          Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
          if (p == rot) rot = b;
          else if (p == forelder.venstre) forelder.venstre = b;
          else forelder.høyre = b;
      }
      else  // Tilfelle 3)
      {
          Node<T> forelder2 = p, r = p.høyre;   // finner neste i inorden
          while (r.venstre != null)
          {
              forelder2 = r;    // s er forelder til r
              r = r.venstre;
          }

          p.verdi = r.verdi;   // kopierer verdien i r til p

          if (forelder2 != p) forelder2.venstre = r.høyre;
          else forelder2.høyre = r.høyre;
      }

      antall--;   // det er nå én node mindre i treet
      return true;
  }

  /////////////////////// OPPGAVE 5 ///////////////////////////////////
  public int fjernAlle(T verdi)
  {
      int atnallVerdier = 0;
      while (inneholder(verdi)){
          fjern(verdi);
          atnallVerdier++;
      }
      return atnallVerdier;

  }
  
  @Override
  public int antall()
  {
    return antall;
  }

    //////////////////////////// OPPGAVE 2 //////////////////////////////////
  public int antall(T verdi)
  {
   int forekomst=0;
    if (verdi == null) throw new NullPointerException();

    Node<T> p = rot;

    while (p!=null){
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp==0){
        forekomst++;
        p=p.høyre;
      }
      else if(cmp<0) p=p.venstre;
      else if (cmp>0) p=p.høyre;
    }


    return forekomst;

  }
  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }
  
  @Override
  public void nullstill()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }

    //////////////////////////// OPPGAVE 3 //////////////////////////////////
  private static <T> Node<T> nesteInorden(Node<T> p)
  {


    int cmp=0;
    Node<T> neste=null;

    // bare en node
      if (p.høyre==null && p.venstre==null && p.forelder==null){

          return p;
      }


    if (p.høyre!=null){
       neste=p.høyre;
      // System.out.println(1);
    }
    else{
      neste=p;
    //  System.out.println(2);
    }
    // p.høyre == null og p == venstrebarn ==== så neste == forelder
    if (p.forelder!=null && p.forelder.venstre!=null && p.høyre==null && p.forelder.venstre.equals(p)){
      neste=p.forelder;
//7      System.out.println(3);

      return neste;
    }

    //


    // hvis man må langt opp i treet hh

    if (p.høyre!=null){
      if (p.høyre.venstre==null){
        // System.out.println(4);

        return p.høyre;

      }
    }
// if bare venstre, og siste har null forelder



   else if (p.forelder!=null && p.forelder.høyre!=null && p.forelder.høyre.equals(p)) {
     while(p.forelder!=null && p.forelder.høyre!= null && p.forelder.høyre.equals((p))){
       p=p.forelder;

     }

     return p.forelder;
    }
    // hvis foreldrene bare er høyre-barn fram til root så er det siste
    // koden under er drit sketchy Jon, ikke døm meg
    // blir aldri kalt på heller tydeligvis
   else{
       // opp til rot
        if (p.forelder==null && p.høyre==null){
         return null;
        }


        if (p.forelder!=null){
            while (p.forelder!=null){
                p=p.forelder;

            }

        }
        // også nedover
        if (p.høyre==null){

            return p;
        }
        while (p.høyre!=null){
            p=p.høyre;
        }

return p;
    }

   // motsatt av over, gå nedover så lagnt som mulig
    p=p.høyre;

   while (p.venstre!=null){
     p=p.venstre;
   }

  return p;



    //System.out.println("returner null");
 //   return null;
  }

    //////////////////////////// OPPGAVE 3 //////////////////////////////////
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    if (rot==null){
      sb.append("[]");
      return sb.toString();
    }

    sb.append("[");
   Node<T> first=rot;
   while (first.venstre!=null){
     first=first.venstre;
   }

   if (first.forelder==null && first.høyre==null && first.venstre==null){
       sb=new StringBuilder();
       sb.append("["+first.verdi+"]");
       return sb.toString();
   }
    //sb.append(first.verdi);
      //TODO: JOOOOOOOOOOOOOOOOOOOON jeg mister det

  // sb.append(first+", ");
   while (nesteInorden(first)!=null){

     sb.append(first.verdi+", ");
     first=nesteInorden(first);
   }
    sb.append(first);
  // sb.deleteCharAt(sb.length()-1);
   //sb.deleteCharAt(sb.length()-1);
   sb.append("]");
return sb.toString();
  }
  
  public String omvendtString()
  {
   TabellStakk<T> stack=new TabellStakk<>();

    if (rot==null){
     return "[]";
    }


    Node<T> first=rot;
    while (first.venstre!=null){
      first=first.venstre;
    }

    if (first.forelder==null && first.høyre==null && first.venstre==null){
    return "["+rot.verdi+"]";
    }
    //sb.append(first.verdi);
    //TODO: JOOOOOOOOOOOOOOOOOOOON jeg mister det

    // sb.append(first+", ");
    while (nesteInorden(first)!=null){

     stack.leggInn(first.verdi);
      first=nesteInorden(first);
    }
    stack.leggInn(first.verdi);

    StringBuilder sb=new StringBuilder();
    sb.append("[");

    while (!stack.tom()){
      sb.append(stack.taUt()+", ");
    }
    sb.deleteCharAt(sb.length()-1);
    sb.deleteCharAt(sb.length()-1);
sb.append("]");
    return sb.toString();

  }
  
  public String høyreGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String lengstGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String[] grener()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String bladnodeverdier()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String postString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public Iterator<T> iterator()
  {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T>
  {
    private Node<T> p = rot, q = null;
    private boolean removeOK = false;
    private int iteratorendringer = endringer;
    
    private BladnodeIterator()  // konstruktør
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
