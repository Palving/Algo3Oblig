////////////////// ObligSBinTre /////////////////////////////////
//Magnus Palving Christiansen - s326302 Mats Grøsvik - s331405 Jon Rafoss - s331379


import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{



  public static void main (String[] args){

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

      if(verdi == null) return false;

      Node<T> p = rot;

      while(p!=null){
          int cmp = comp.compare(verdi,p.verdi);

          if(cmp < 0) p=p.venstre;
          else if(cmp > 0) p=p.høyre;
          else break;
      }

      if (p==null) return false;

      if (p.venstre==null || p.høyre==null) {

          Node<T> b = (p.venstre!=null) ? p.venstre : p.høyre;

          if (p == rot) {
              rot =  b;
              if(b!=null) b.forelder=null;
          }
          else if (p==p.forelder.venstre) {
              if(b!=null)b.forelder = p.forelder;
              p.forelder.venstre = b;
          } else {

              if(b!=null)b.forelder = p.forelder;
              p.forelder.høyre = b;
          }
      }
      else {

          Node<T> r = p.høyre;
          while (r.venstre != null) r = r.venstre;
          p.verdi = r.verdi;

          if(r.forelder!=p) {
              Node<T> q = r.forelder;
              q.venstre = r.høyre;
              if(q.venstre!=null)q.venstre.forelder = q;
          }
          else{
              p.høyre =  r.høyre;
              if(p.høyre !=null) p.høyre.forelder = p;

          }
      }

      antall--;
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

  //////////////////////////// OPPGAVE 5 ////////////////////////////////////
  @Override
  public void nullstill()
  {
    Node<T> p = rot;

    if(rot == null){
        return;
    }
    while (p.venstre != null){
        p = p.venstre;
    }


    Node<T> s = p; // hjelpenode

    while (nesteInorden(p) != null){
        s = nesteInorden(p);
        if(fjern(p.verdi)){
            p=s;
        }
        else{
            break;
        }
    }

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
      StringBuilder ut = new StringBuilder();
      ut.append("[");
      Node p = rot;

      if(rot != null){

          ut.append(p);

          while(p.høyre != null || p.venstre!=null ){
              if(p.høyre != null) {
                  p = p.høyre;
              }
              else {
                  p = p.venstre;
              }
              ut.append(",").append(" ").append(p);
          }
      }
      ut.append("]");
      return ut.toString();

  }


  /////////////////////////////// OPPGAVE 6 /////////////////////////
  public String lengstGren() {

      // while neste inorden er blad node, loop opp til root og tell antall loops
      Node<T> first = rot;

      if (rot==null){
          return "[]";
      }

      while (first.venstre != null) {
          first = first.venstre;
      }

      // first = starter på første inorden

      StringBuilder grenToCmp=new StringBuilder();
      StringBuilder gren=new StringBuilder();
      int antall = 0;
      int antallToCmp = 0;
      Node<T> neste = first;

      while (neste.forelder != null) {
       //   gren.append(neste.verdi+", ");
             gren.insert(0,neste.verdi+", ");
          antall++;
          neste = neste.forelder;
      }


      //System.out.print("første"+neste);
        Node<T> blad;

      if (rot.høyre==null){
          gren.insert(0,rot.verdi+", ");
          gren.deleteCharAt(gren.length()-1);
          gren.deleteCharAt(gren.length()-1);
          gren.insert(0,"[");
          gren.append("]");

          return gren.toString();
      }

      while (nesteInorden(neste)!=null){
          // finn neste bladnode
          if (nesteInorden(neste).venstre==null && nesteInorden(neste).høyre==null){
              // funnet bladnode
              // tell antall steg opp til root
              blad=nesteInorden(neste);
            antallToCmp=0;
            grenToCmp=new StringBuilder();



              while(blad.forelder!=null){
                  antallToCmp++;

                 // grenToCmp.append(blad.verdi+", ");
                    grenToCmp.insert(0,blad.verdi+", ");
                  blad=blad.forelder;
              }
              if (antallToCmp>antall){
                  //System.out.println(grenToCmp+"antall="+antallToCmp);
                  antall=antallToCmp;
                 gren=grenToCmp;


              }

          }
          else{
              // neste er nesteinorden
             // neste=nesteInorden(neste);
          }
          neste=nesteInorden(neste);
      }
      gren.deleteCharAt(gren.length()-1);
      gren.deleteCharAt(gren.length()-1);
gren.insert(0,"["+rot+", ");
      gren.append("]");
  return gren.toString();

  }

  /////////////////////////////// OPPGAVE 7 ////////////////////////////////////
  public String[] grener()
  {
      String[] tabell=new String[0];

      if (rot==null){
        return tabell;
      }

       tabell=new String[1];
      if (rot.høyre==null && rot.venstre==null){
          tabell[0]="["+rot+"]";
          return tabell;
      }

      // while neste inorden er blad node, loop opp til root og tell antall loops
      Node<T> first = rot;

      while (first.venstre != null) {
          first = first.venstre;
      }

      // first = starter på første inorden


      StringBuilder gren=new StringBuilder();
      int indeks = 0;
      int antallGrener=0;
      Node<T> neste = first;

      Node<T> blad;

      while (nesteInorden(neste)!=null) {
       // finn antall bladnoder
          if (nesteInorden(neste).venstre == null && nesteInorden(neste).høyre == null) {
              // funnet bladnode

            //  blad = nesteInorden(neste);
             antallGrener++;

          }
          neste = nesteInorden(neste);
      }

      tabell=new String[antallGrener];
   // System.out.println("antall grener"+antallGrener);
      // første gren
      neste=first;
      while (neste.forelder != null) {
          gren.insert(0,neste.verdi+", ");

          neste = neste.forelder;
      }
      //int teller=1;
      gren.insert(0,"["+rot.verdi+", ");
      gren.deleteCharAt(gren.length()-1);
      gren.deleteCharAt(gren.length()-1);
      gren.append("]");

      tabell[indeks]=gren.toString();
      indeks++;

      neste=first;


      while (nesteInorden(neste)!=null) {
          // finn neste bladnode
          if (nesteInorden(neste).venstre == null && nesteInorden(neste).høyre == null) {
              // funnet bladnode
              gren=new StringBuilder();

              blad = nesteInorden(neste);

              while(blad.forelder!=null) {
                  gren.insert(0, blad.verdi + ", ");
                  blad=blad.forelder;
              }
             // teller++;
              gren.insert(0,"["+rot.verdi+", ");
              gren.deleteCharAt(gren.length()-1);
              gren.deleteCharAt(gren.length()-1);
              gren.append("]");

              tabell[indeks-1]=gren.toString();
              indeks++;

          }

         // System.out.println(teller);
          neste = nesteInorden(neste);
      }

      return tabell;

  }

  ///////////////////////////////// OPPGAVE 8a ///////////////////////////
  public String bladnodeverdier()
  {
      if (rot==null){
          return "[]";
      }
   StringBuilder sb=new StringBuilder("[");

        bladnodeverdi(rot,sb);
        sb.deleteCharAt(sb.length()-1);
      sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();


  }


  private void bladnodeverdi(Node<T> node, StringBuilder sb) {
   if (node==null){
       return;
   }

if (node.venstre==null && node.høyre==null){
    sb.append(node.verdi+", ");
}

bladnodeverdi(node.venstre, sb);
bladnodeverdi(node.høyre,sb);


  }
  
  public String postString()
  {

StringBuilder ut=new StringBuilder();
boolean ok=true;
Node first=rot;

if (rot==null) return "[]";
if (first.høyre==null && first.venstre==null)
{
    return "["+first+"]";
}
    //TabellStakk stack=new TabellStakk();
    Stack<Node> stack=new Stack();

while (true){
while (rot!=null) {
    stack.push(rot);
    stack.push(rot);
    rot = rot.venstre;
}
if (stack.empty()){
    ut.deleteCharAt((ut.length())-1);
    ut.deleteCharAt((ut.length())-1);
    return "["+ut.toString()+"]";
}
rot=stack.pop();
if (!stack.empty() && stack.peek() == rot) rot=rot.høyre;

else{
  //  System.out.println(rot.verdi); rot=null;
   // System.out.println(rot.verdi);
   ut.append(rot.verdi+", "); rot = null;
    }

      }
      //return ut.toString();
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
         if (p == null)
         {
             return;
         }

         while (true)
         {
             if (p.venstre != null)
             {
                 p = p.venstre;
             }
             else if (p.høyre != null)
             {
                 p = p.høyre;
             }
             else
             {
                 break;  // p er en bladnode
             }
         }
     }

     @Override
     public boolean hasNext()
     {
         return p != null;
     }

     @Override
     public T next()
     {
         if (!hasNext())
         {
             throw new NoSuchElementException("Ikke flere verdier!");
         }

         removeOK = true;

         q = p;

         while (true)
         {
             p = nesteInorden(p);
             if (p == null || (p.venstre == null && p.høyre == null))
             {
                 break;
             }
         }

         return q.verdi;
     }

     @Override
     public void remove() {
         if (!removeOK) throw new IllegalStateException("Ulovlig tilstand!");
         else if (endringer != iteratorendringer) throw new ConcurrentModificationException("");
         removeOK = false;

         if (antall == 1) {
             q = p = null;
         } else {
             if (q.forelder.venstre == q) q.forelder.venstre = null;
             else q.forelder.høyre = null;
         }

         antall--;
         endringer++;
         iteratorendringer++;
     }

 } // BladnodeIterator

} // ObligSBinTre
