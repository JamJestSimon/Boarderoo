using Google.Type;

namespace BoarderooAPI.Model;
public class Order
{
    public string Id {get; set;}
    public System.DateTime Start {get; set;}
    public System.DateTime End {get; set;}
    public StatusType Status {get; set;} //wybieralne z listy
    public User User{get; set;}
    public List<Game> Items{get; set;}

    public float Price {get; set;}

}

public enum StatusType
{
    Zamówione,
    Zapłacone,
    Potwierdzone,
    Anulowane,
    Zakończone,
}