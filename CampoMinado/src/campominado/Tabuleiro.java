package campominado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.util.Pair;



public class Tabuleiro 
{
    private int qtdMinas;	
    private Celulas cells[][];

    private int linhas;
    private int colunas;

        
    //---------------------------------------------//
    
    public Tabuleiro(int qtdMinas, int r, int c)
    {
        this.linhas = r;
        this.colunas = c;
        this.qtdMinas = qtdMinas;

        cells = new Celulas[linhas][colunas];

        criaCelulasVazias();         

        setMinas();

        setQtdMinasRedor();
    }


    //------------------------------------------------------------------//
    public void criaCelulasVazias()
    {
        for (int x = 0; x < colunas; x++)
        {
            for (int y = 0; y < linhas; y++)
            {
                cells[x][y] = new Celulas();
            }
        }
    }

    //------------------------------------------------------------------//

    public void setMinas()
    {
        int x,y;
        boolean temMina;
        int minasAtuais = 0;                

        while (minasAtuais != qtdMinas)
        {
            x = (int)Math.floor(Math.random() * colunas);
            y = (int)Math.floor(Math.random() * linhas);

            temMina = cells[x][y].getMina();

            if(!temMina)
            {		
                cells[x][y].setMina(true);
                minasAtuais++;	
            }			
        }
    }
    //------------------------------------------------------------------//

    //------------------------------------------------------------------//

    public void setQtdMinasRedor()
    {	
        for(int x = 0 ; x < colunas ; x++) 
        {
            for(int y = 0 ; y < linhas ; y++) 
            {
                cells[x][y].setQtdMinas(calculaVizinhos(x,y));                        
            }
        }
    }
    //------------------------------------------------------------------//	


    public int calculaVizinhos(int xCo, int yCo)
    {
        int vizinhos = 0;

        for(int x=fazerCoordenadaValidaX(xCo - 1); x<=fazerCoordenadaValidaX(xCo + 1); x++) 
        {

            for(int y=fazerCoordenadaValidaY(yCo - 1); y<=fazerCoordenadaValidaY(yCo + 1); y++) 
            {

                if(x != xCo || y != yCo)
                    if(cells[x][y].getMina())
                        vizinhos++;
            }
        }

        return vizinhos;
    }

    //------------------------------------------------------------------//	

    public int fazerCoordenadaValidaX(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > colunas-1)
            i = colunas-1;

        return i;
    }	
    
    public int fazerCoordenadaValidaY(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > linhas-1)
            i = linhas-1;

        return i;
    }	
    
    //------------------------------------------------------------------//	        

    
    public void setQtdMinas(int qtdMinas)
    {
        this.qtdMinas = qtdMinas;
    }

    public int getQtdMinas()
    {
        return qtdMinas;
    }

    public Celulas[][] getCells()
    {
        return cells;
    }
    
    public int getLinhas()
    {
        return linhas;
    }
    
    public int getColunas()
    {
        return colunas;
    }
    //-----------------------------------------//

    public void resetaTabuleiro()
    {
        for(int x = 0 ; x < colunas ; x++) 
        {
            for(int y = 0 ; y < linhas ; y++) 
            {
                cells[x][y].setConteudo("");                        
            }
        }
    }
    
}
