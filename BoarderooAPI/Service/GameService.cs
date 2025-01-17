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

    public async Task<ServiceResult<Game>> AddGame(Game game)
    {
         try{
        var Collection = getGameCollection();
        var data = await Collection.GetSnapshotAsync();
         if(data.Documents.Count<1)
        {
            await Collection.AddAsync(game);
              return new ServiceResult<Game>
        {
            Message="Gra dodana poprawnie!",
            ResultCode=200,
            Data=game
        };
    }
        else return new ServiceResult<Game>
        {
            Message="Juz istnieje gra o takiej nazwie!",
            ResultCode=409
        }; //409
        }
        catch (Exception e)
        {
            return new ServiceResult<Game>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<Game>> UpdateGame(Game game)
    {
        try
        {
            var Collection= getGameCollectionById(game.Id); 
            var data = await Collection.GetSnapshotAsync();
             if (!data[0].Exists)
        {
            return new ServiceResult<Game>
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
            };

            DocumentReference gameDocument=data.Documents[0].Reference;

            await gameDocument.UpdateAsync(gamedict);
            var gamesCollection = this.getGameCollectionById(game.Id);
            var updatedGame = data[0].ConvertTo<Game>();

             return new ServiceResult<Game>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=updatedGame
        };
        }
        }
        catch(Exception e)
        {
                    return new ServiceResult<Game>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }

    }

    public async Task<ServiceResult<Game>> DeleteGame(string id)
    {
        try
        {
            var Collection = getGameCollectionById(id);
            var data = await Collection.GetSnapshotAsync();
            if (!data[0].Exists)
        {
            return new ServiceResult<Game>
        {
            Message="Brak gry o takim emailu!",
            ResultCode=404
        };}
        else
        {
            DocumentReference gameDocument=data.Documents[0].Reference;

            await gameDocument.DeleteAsync();
            return new ServiceResult<Game>
        {
            Message="Uzytkownik usuniety pomyslnie!",
            ResultCode=200
        };
        }
            
        }
        catch(Exception e)
        {
        return new ServiceResult<Game>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }
    public async Task DisableGame(string id)
    {

    }

    public async Task<ServiceResult<List<Game>>> GetAllGames()
    {
        var games = new List<Game>();

        try
        {
            var gamesCollection = getGameCollection();
            //tutaj popraw jesli jest pusta
            if(gamesCollection!=null)
            {
                var gameList = await gamesCollection.GetSnapshotAsync();
                foreach (var game in gameList.Documents)
                {
                    Game g = new Game();
                    g.Name=game.GetValue<string>("Name");
                    g.Type=game.GetValue<string>("Type");
                    g.Description=game.GetValue<string>("Description");
                    g.Publisher=game.GetValue<string>("Publisher");
                    g.Players_number=game.GetValue<string>("Players_Number");
                    g.Rating=game.GetValue<int>("Rating");
                    g.Year=game.GetValue<string>("Year");
                    //g.Enabled=game.GetValue<bool>("Enabled");

                    games.Add(g);
                }
            }
            else
            {
                     return new ServiceResult<List<Game>>
            {
            Message="Brak gier!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<Game>>
            {
            Message="Gry pobrane pomyslnie!",
            ResultCode=200,
            Data=games
            };
        }
        catch(Exception e)
        {
           return new ServiceResult<List<Game>>
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
    public Google.Cloud.Firestore.Query getGameCollectionById(string id)
    {
        return _database.Collection("games").WhereEqualTo("Id", id);
    }
    public Google.Cloud.Firestore.DocumentReference getUserCollectionById(string id)
    {
        return _database.Collection("games").Document(id);
    }
 }