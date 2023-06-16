package br.ufscar.dc.dsw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AcessaBD {

    public static void main(String[] args) {
        try {

            /* Setup para uso do banco de dados MySQL */
            Class.forName("com.mysql.cj.jdbc.Driver");
            // String url = "jdbc:mysql://localhost:3306/Livraria";
            String url = "jdbc:mysql://localhost:3306/Livraria?useTimezone=true&serverTimezone=UTC";
            Connection con = (Connection) DriverManager.getConnection(url, "root", "root");
            /* Setup para uso do banco de dados Derby */

            /*
             * Class.forName("org.apache.derby.jdbc.ClientDriver");
             * String url = "jdbc:derby://localhost:1527/Livraria";
             * Connection con = (Connection) DriverManager.getConnection(url,
             * "root", "root");
             */

            Statement stmt = con.createStatement();
            // Adicionando a nova editora
            String cnpj = "87.557.922/0001-82";
            String nome = "Seguinte";
            String sql = "INSERT INTO Editora (CNPJ, Nome) VALUES ('" + cnpj + "', '" + nome + "')";
            stmt.executeUpdate(sql);

            // Inserir novo livro
            inserirLivro(stmt, "Seguinte", "O Dia do Curinga", "Jostein Gaarder", 1996, 129.99f);
            inserirLivro(stmt, "Companhia das Letras", "A Revolução dos Bichos", "George Orwell", 2007, 123.90f);

            // Recuperando e exibindo os livros após a adição da editora
            ResultSet rs = stmt.executeQuery("SELECT * FROM Livro ORDER BY Preco");
            while (rs.next()) {
                System.out.print(rs.getString("Titulo"));
                System.out.print(", " + rs.getString("Autor"));
                System.out.print(", " + rs.getInt("Ano"));
                System.out.println(" (R$ " + rs.getFloat("Preco") + ")");
            }
            stmt.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("A classe do driver de conexão não foi encontrada!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("O comando SQL não pode ser executado!");
        }
    }

    private static void inserirLivro(Statement stmt, String editora, String titulo, String autor, int ano, float preco) throws SQLException {
        // Verificar o ID da editora
        String sqlEditora = "SELECT id FROM Editora WHERE Nome = '" + editora + "'";
        ResultSet rsEditora = stmt.executeQuery(sqlEditora);
        if (rsEditora.next()) {
            int editoraId = rsEditora.getInt("id");

            // Adicionar o novo livro
            String sqlLivro = "INSERT INTO Livro (titulo, autor, ano, preco, editora_id) VALUES ('" + titulo + "', '" + autor + "', " + ano + ", " + preco + ", " + editoraId + ")";
            stmt.executeUpdate(sqlLivro);
        } else {
            System.out.println("A editora '" + editora + "' não existe no banco de dados.");
        }
    }
}
