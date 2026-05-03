using System;

namespace DiamondInventory.Models;

public class Diamond
{
    public long Id { get; set; }
    public string Nom { get; set; } = "";
    public string Carats { get; set; } = "";
    public string Prix { get; set; } = "";
    public string Couleur { get; set; } = "";
    public string Clarte { get; set; } = "";
    public string Notes { get; set; } = "";
    public int Stock { get; set; } = 1;
    public string? ImagePath { get; set; }
    public long Timestamp { get; set; } = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
    public bool IsOutOfStock => Stock <= 0;
}
