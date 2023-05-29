import java.sql.*;

public class DatabaseHandler {
    private Connection conn;

    public DatabaseHandler(String dbPath) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS usuarios (nome TEXT, cpf TEXT, senha TEXT)");
    }

    public void addUser(String nome, String cpf, String senha) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("INSERT INTO usuarios VALUES (?, ?, ?)");
        statement.setString(1, nome);
        statement.setString(2, cpf);
        statement.setString(3, senha);
        statement.execute();
    }

    public void updateUser(String nome, String cpf, String senha) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE usuarios SET nome=?, cpf=?, senha=? WHERE nome=?");
        statement.setString(1, nome);
        statement.setString(2, cpf);
        statement.setString(3, senha);
        statement.setString(4, nome);
        statement.execute();
    }

    public void deleteUser(String nome) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM usuarios WHERE nome=?");
        statement.setString(1, nome);
        statement.execute();
    }

    public ResultSet getUsers() throws SQLException {
        Statement statement = conn.createStatement();
        return statement.executeQuery("SELECT * FROM usuarios");
    }
}
