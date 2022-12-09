/*
   Estefany Castro Agudelo 201958552 - 3743
   Valentina Hurtado Garcia 201958542 - 3743
 */

package rojinegros;

import lombok.Getter;
import lombok.Setter;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.Queue;

public class ArbolRojinegro {
    @Getter
    @Setter
    private ArbolRojinegro izq;

    @Getter
    @Setter
    private ArbolRojinegro der;

    @Getter
    @Setter
    private ArbolRojinegro father;

    @Getter
    @Setter
    private int valor;

    @Getter
    @Setter
    private boolean black; //Si es negro True, en otro caso rojo

    public ArbolRojinegro(ArbolRojinegro izq,
                          ArbolRojinegro der,
                          int valor,
                          boolean black) {
        this.izq = izq;
        this.der = der;
        this.valor = valor;
        this.black = black;
    }

    public ArbolRojinegro() {
        this.izq = null;
        this.der = null;
        this.black = true;
    }

    /*
     * Metodos a implementar
     */
    public void insertar(int x) throws Exception {
        if (this.valor == x) {
            // ...
        } else if (this.valor < x && this.der != null) {
            this.der.insertar(x);
        } else if (this.valor < x && this.der == null) {
            ArbolRojinegro n = new ArbolRojinegro(null, null, x, false);
            this.der = n;
            nuevoOrden(n);
        } else if (this.valor > x && this.izq != null) {
            this.izq.insertar(x);
        } else if (this.valor > x && this.izq == null) {
            ArbolRojinegro n = new ArbolRojinegro(null, null, x, false);
            this.izq = n;
            nuevoOrden(n);
        }
    }

    public void nuevoOrden(ArbolRojinegro nuevo) {
        try {
            while (nuevo.getFather().isBlack() == false) {
                ArbolRojinegro padre = nuevo.getFather();
                ArbolRojinegro abuelo = padre.getFather();
                ArbolRojinegro tio;
                if (abuelo.getDer() == padre) {
                    tio = abuelo.getIzq();
                } else {
                    tio = abuelo.getDer();
                }
                if (padre.isBlack() == false && tio.isBlack() == false) {
                    padre.setBlack(true);
                    tio.setBlack(true);
                    abuelo.setBlack(false);
                } else if (padre.isBlack() == false && tio.isBlack() && nuevo == padre.getDer()) {
                    nuevo = padre;
                    nuevo.rotacionIzquierda(nuevo.getValor());
                } else if (padre.isBlack() == false && tio.isBlack() && nuevo == padre.getIzq()) {
                    padre.setBlack(true);
                    abuelo.setBlack(false);
                    abuelo.rotacionDerecha(abuelo.getValor());
                }
                ArbolRojinegro raiz = nuevo.root();
                raiz.setBlack(true);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public int maximo() throws Exception {
        if (this.getDer() != null) {
            return this.getDer().maximo();
        } else {
            return valor;
        }
    }

    public int minimo() throws Exception {
        if (this.getIzq() != null) {
            return this.getIzq().minimo();
        } else {
            return valor;
        }
    }

    public ArbolRojinegro search(int x) throws Exception {
        if (this.valor == x) {
            return this;
        } else if (this.valor < x && this.der != null) {
            return this.der.search(x);
        } else if (this.valor > x && this.izq != null) {
            return this.izq.search(x);
        } else {
            return null;
        }
    }

    //Rotacion izquierda
    public void rotacionIzquierda(int x) throws Exception {
        if (this.valor == x) {
            //Rotacion ---------
            ArbolRojinegro n = this.der;
            this.der = this.der.getIzq();
            n.setIzq(this);
            n.setFather(this.father);
            if (this.father != null) {
                if (this.father.getIzq() == this) {
                    this.father.setIzq(n);
                } else {
                    this.father.setDer(n);
                }
                this.father = n;
            } else {
                this.father = n;
                corregirIzq();
            }
            //--------------------
        } else if (this.valor < x && this.der != null) {
            this.der.rotacionIzquierda(x);
        } else if (this.valor > x && this.izq != null) {
            this.izq.rotacionIzquierda(x);
        }
    }

    //Busca la referencia de quien es la raiz
    public ArbolRojinegro root() {
        if (father == null) {
            return this;
        } else {
            return father.root();
        }
    }

    //Genera el nuevo arbol y cambia las referencias, especialmente la raiz para que funcione
    public void corregirIzq() {
        ArbolRojinegro raiz = this.root();
        ArbolRojinegro tmpFather = this.father;
        ArbolRojinegro tmpIzq = this.izq;
        ArbolRojinegro tmpDer = this.der;
        int tmpValor = this.valor;

        this.father = null;
        this.izq = raiz;
        this.der = raiz.getDer();
        this.valor = raiz.getValor();

        raiz.setFather(tmpFather);
        raiz.setIzq(tmpIzq);
        raiz.setDer(tmpDer);
        raiz.setValor(tmpValor);
    }

    public void rotacionDerecha(int x) throws Exception {
        if (this.valor == x) {
            //Rotacion ---------
            ArbolRojinegro n = this.izq;
            this.izq = this.izq.getDer();
            n.setDer(this);
            n.setFather(this.father);
            if (this.father != null) {
                if (this.father.getDer() == this) {
                    this.father.setDer(n);
                } else {
                    this.father.setIzq(n);
                }
                this.father = n;
            } else {
                this.father = n;
                corregirDer();
            }
            //--------------------
        } else if (this.valor < x && this.der != null) {
            this.der.rotacionDerecha(x);
        } else if (this.valor > x && this.izq != null) {
            this.izq.rotacionDerecha(x);
        }
    }

    public void corregirDer() {
        ArbolRojinegro raiz = this.root();
        ArbolRojinegro tmpFather = this.father;
        ArbolRojinegro tmpIzq = this.izq;
        ArbolRojinegro tmpDer = this.der;
        int tmpValor = this.valor;

        this.father = null;
        this.izq = raiz.getIzq();
        this.der = raiz;
        this.valor = raiz.getValor();

        raiz.setFather(tmpFather);
        raiz.setIzq(tmpIzq);
        raiz.setDer(tmpDer);
        raiz.setValor(tmpValor);
    }

    /*
     *  Area de pruebas, no modificar.
     */
    //Verificaciones
    /*
     * Busqueda por amplitud para verificar arbol.
     */
    public String bfs() {
        String salida = "";
        String separador = "";
        Queue<ArbolRojinegro> cola = new LinkedList<>();
        cola.add(this);
        while (cola.size() > 0) {
            ArbolRojinegro nodo = cola.poll();
            salida += separador + String.valueOf(nodo.getValor());
            separador = " ";
            if (nodo.getIzq() != null) {
                cola.add(nodo.getIzq());
            }
            if (nodo.getDer() != null) {
                cola.add(nodo.getDer());
            }
        }
        return salida;
    }

    /*
     * Recorrido inorder.
     * Verifica propiedad de orden.
     */
    public String inorden() {
        String recorrido = "";
        String separador = "";
        if (this.getIzq() != null) {
            recorrido += this.getIzq().inorden();
            separador = " ";
        }
        recorrido += separador + String.valueOf(this.getValor());
        if (this.getDer() != null) {
            recorrido += " " + this.getDer().inorden();
        }
        return recorrido;
    }

}