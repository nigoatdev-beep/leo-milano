using Microsoft.Data.Sqlite;
using DiamondInventory.Models;

namespace DiamondInventory.Services;

public class DatabaseService
{
    private static readonly string DbPath = Path.Combine(
        Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData),
        "DiamondInventory", "diamonds.db");

    private SqliteConnection GetConnection() => new($"Data Source={DbPath}");

    public DatabaseService()
    {
        Directory.CreateDirectory(Path.GetDirectoryName(DbPath)!);
        using var conn = GetConnection();
        conn.Open();
        var cmd = conn.CreateCommand();
        cmd.CommandText = @"
            CREATE TABLE IF NOT EXISTS diamonds (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                carats TEXT,
                prix TEXT,
                couleur TEXT,
                clarte TEXT,
                notes TEXT,
                stock INTEGER DEFAULT 1,
                image_path TEXT,
                timestamp INTEGER
            )";
        cmd.ExecuteNonQuery();
    }

    public List<Diamond> GetAll()
    {
        var list = new List<Diamond>();
        using var conn = GetConnection();
        conn.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT * FROM diamonds ORDER BY timestamp DESC";
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            list.Add(ReadDiamond(reader));
        }
        return list;
    }

    public Diamond? GetById(long id)
    {
        using var conn = GetConnection();
        conn.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT * FROM diamonds WHERE id = $id";
        cmd.Parameters.AddWithValue("$id", id);
        using var reader = cmd.ExecuteReader();
        return reader.Read() ? ReadDiamond(reader) : null;
    }

    public long Insert(Diamond d)
    {
        using var conn = GetConnection();
        conn.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = @"
            INSERT INTO diamonds (nom, carats, prix, couleur, clarte, notes, stock, image_path, timestamp)
            VALUES ($nom, $carats, $prix, $couleur, $clarte, $notes, $stock, $image, $ts);
            SELECT last_insert_rowid();";
        AddParams(cmd, d);
        return (long)cmd.ExecuteScalar()!;
    }

    public void Update(Diamond d)
    {
        using var conn = GetConnection();
        conn.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = @"
            UPDATE diamonds SET nom=$nom, carats=$carats, prix=$prix, couleur=$couleur,
            clarte=$clarte, notes=$notes, stock=$stock, image_path=$image, timestamp=$ts
            WHERE id = $id";
        cmd.Parameters.AddWithValue("$id", d.Id);
        AddParams(cmd, d);
        cmd.ExecuteNonQuery();
    }

    public void Delete(long id)
    {
        using var conn = GetConnection();
        conn.Open();
        using var cmd = conn.CreateCommand();
        cmd.CommandText = "DELETE FROM diamonds WHERE id = $id";
        cmd.Parameters.AddWithValue("$id", id);
        cmd.ExecuteNonQuery();
    }

    private static void AddParams(SqliteCommand cmd, Diamond d)
    {
        cmd.Parameters.AddWithValue("$nom", d.Nom);
        cmd.Parameters.AddWithValue("$carats", d.Carats);
        cmd.Parameters.AddWithValue("$prix", d.Prix);
        cmd.Parameters.AddWithValue("$couleur", d.Couleur);
        cmd.Parameters.AddWithValue("$clarte", d.Clarte);
        cmd.Parameters.AddWithValue("$notes", d.Notes);
        cmd.Parameters.AddWithValue("$stock", d.Stock);
        cmd.Parameters.AddWithValue("$image", d.ImagePath ?? (object)DBNull.Value);
        cmd.Parameters.AddWithValue("$ts", d.Timestamp);
    }

    private static Diamond ReadDiamond(SqliteDataReader r) => new()
    {
        Id = r.GetInt64(0),
        Nom = r.GetString(1),
        Carats = r.IsDBNull(2) ? "" : r.GetString(2),
        Prix = r.IsDBNull(3) ? "" : r.GetString(3),
        Couleur = r.IsDBNull(4) ? "" : r.GetString(4),
        Clarte = r.IsDBNull(5) ? "" : r.GetString(5),
        Notes = r.IsDBNull(6) ? "" : r.GetString(6),
        Stock = r.IsDBNull(7) ? 1 : r.GetInt32(7),
        ImagePath = r.IsDBNull(8) ? null : r.GetString(8),
        Timestamp = r.IsDBNull(9) ? 0 : r.GetInt64(9)
    };
}
