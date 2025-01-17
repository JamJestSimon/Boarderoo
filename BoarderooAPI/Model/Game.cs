namespace BoarderooAPI.Model;
public class Game
{
    public string Id {get; set;}
    public string Name {get; set;}
    public string Type {get; set;}
    public float Price {get; set;}
    public string Description {get; set;}
    public string Publisher {get; set; }
    public string Players_number {get; set;}
    public string Year { get; set;}
    public int Rating {get; set;}
    public bool Enabled {get; set;}
    public int Available_copies {get; set;}
}