 using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
 namespace BoarderooAPI.Service;
 public class GameService
 {

 
 private readonly FirestoreDb _database;

    public GameService(FireBaseService firebaseService)
    {
        _database = firebaseService.getDatabase(); 
    }

    public async Task<ServiceResult<GameDocument>> AddGame(GameDocument game)
    {
         try{
        var Collection = getGameCollectionByName(game.Name);
        var data = await Collection.GetSnapshotAsync();
         if(data.Documents.Count<1)
        {
            var gamesCollection=getGameCollection();

            await gamesCollection.AddAsync(game);
              return new ServiceResult<GameDocument>
        {
            Message="Gra dodana poprawnie!",
            ResultCode=200,
            Data=game
        };
        }
        else return new ServiceResult<GameDocument>
        {
            Message="Juz istnieje gra o takiej nazwie!",
            ResultCode=409
        }; //409
        }
        catch (Exception e)
        {
            return new ServiceResult<GameDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<GameDocument>> UpdateGame(GameDocument game)
    {
        try
        {
            var Collection= getGameCollectionById(game.Id); 
            var data = await Collection.GetSnapshotAsync();
             if (!data.Exists)
        {
            return new ServiceResult<GameDocument>
        {
            Message="Brak gry o takim id!",
            ResultCode=404
        };
        }
        else
        {
            Dictionary<string, object> gamedict = new Dictionary<string, object>()
            {
                { "Name",game.Name},
                { "Type",game.Type },
                { "Description",game.Description },
                { "Publisher",game.Publisher },
                { "Players_number",game.Players_number },
                { "Year",game.Year },
                { "Rating",game.Rating },
                {"Price",game.Price},
                {"Available_copies",game.Available_copies}
            };

            DocumentReference gameDocument=data.Reference;

            await gameDocument.UpdateAsync(gamedict);
            var gamesCollection = getGameCollectionById(game.Id);
            var updatedGame = data.ConvertTo<GameDocument>();

             return new ServiceResult<GameDocument>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=updatedGame
        };
        }
        }
        catch(Exception e)
        {
                    return new ServiceResult<GameDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }

    }

    public async Task<ServiceResult<GameDocument>> DeleteGame(string id)
    {
        try
        {
            var Collection = getGameCollectionById(id);
            var data = await Collection.GetSnapshotAsync();
            if (!data.Exists)
        {
            return new ServiceResult<GameDocument>
        {
            Message="Brak gry o takim id!",
            ResultCode=404
        };}
        else
        {
            DocumentReference gameDocument=data.Reference;

            await gameDocument.DeleteAsync();
            return new ServiceResult<GameDocument>
        {
            Message="Uzytkownik usuniety pomyslnie!",
            ResultCode=200
        };
        }
            
        }
        catch(Exception e)
        {
        return new ServiceResult<GameDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }
    public async Task DisableGame(string id)
    {

    }
     public async Task<ServiceResult<GameDocument>>GetGame(string id)
    {
        try
        {
            var Collection=getGameCollectionById(id);
            var data=await Collection.GetSnapshotAsync();
            if (!data.Exists)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<GameDocument>
        {
            Message="Brak gry o takim id!",
            ResultCode=404
        };
        }
        else
        {
            var game = data.ConvertTo<GameDocument>();
            //var help=ConvertDocumentToModel(user);
             return new ServiceResult<GameDocument>
        {
            Message="Uzytkownik pobrany poprawnie!",
            ResultCode=200,
            Data=game
        };
        }

        }
        catch (Exception e)
        {
            return new ServiceResult<GameDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<List<GameDocument>>> GetAllGames()
    {
        var games = new List<GameDocument>();

        try
        {
            var gamesCollection = getGameCollection();
            //tutaj popraw jesli jest pusta
            if(gamesCollection!=null)
            {
                var gameList = await gamesCollection.GetSnapshotAsync();
                foreach (var game in gameList.Documents)
                {
                    GameDocument g = new GameDocument();
                    g.Id=game.Id;
                    g.Name=game.GetValue<string>("Name");
                    g.Type=game.GetValue<string>("Type");
                    g.Description=game.GetValue<string>("Description");
                    g.Publisher=game.GetValue<string>("Publisher");
                    g.Players_number=game.GetValue<string>("Players_number");
                    g.Rating=game.GetValue<string>("Rating");
                    g.Year=game.GetValue<string>("Year");
                    g.Enabled=game.GetValue<bool>("Enabled");
                    g.Available_copies=game.GetValue<int>("Available_copies");
                    g.Price=game.GetValue<float>("Price");
                    games.Add(g);
                }
            }
            else
            {
                     return new ServiceResult<List<GameDocument>>
            {
            Message="Brak gier!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<GameDocument>>
            {
            Message="Gry pobrane pomyslnie!",
            ResultCode=200,
            Data=games
            };
        }
        catch(Exception e)
        {
           return new ServiceResult<List<GameDocument>>
            {
            Message="Blad"+e.ToString(),
            ResultCode=500
            };     
        }
    }

    public Google.Cloud.Firestore.CollectionReference getGameCollection()
    {
        return _database.Collection("games");
    }
    public Google.Cloud.Firestore.DocumentReference getGameCollectionById(string id)
    {
        return _database.Collection("games").Document(id);
    }
    public Google.Cloud.Firestore.Query getGameCollectionByName(string name)
    {
        return _database.Collection("games").WhereEqualTo("Name", name);
    }
  

 }