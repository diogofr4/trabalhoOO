package campominado;



public class Celulas 
{
    private boolean mina;

    /*
     * O conteúdo da célula pode ser:
     *  "" - campo vazio
     *  "F" - campo com bandeira
     *  "M" - mina
     *  um número de 0 a 8 - indicando a quantidade de minas
     */

    private String conteudo;
    private int qtdMinas;

    
    //----------------------------------------------------------//

    public Celulas()
    {
        mina = false;
        conteudo = "";
        qtdMinas = 0;
    }

    public boolean getMina()
    {
        return mina;
    }

    public void setMina(boolean mina)
    {
        this.mina = mina;
    }

    public String getConteudo()
    {
        return conteudo;
    }

    public void setConteudo(String conteudo)
    {
        this.conteudo = conteudo;
    }

    public int getQtdMinas()
    {
        return qtdMinas;
    }

    public void setQtdMinas(int qtdMinas)
    {
        this.qtdMinas = qtdMinas;
    }

    //-------------------------------------------------------------//
}
