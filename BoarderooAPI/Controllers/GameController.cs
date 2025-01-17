using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]
public class GameController:ControllerBase
{
    private readonly FireBaseService _firebaseService;
    private readonly GameService _gameService;
    public GameController(GameService gameService)
    {
       this._gameService=gameService; 
    }

    [HttpGet(Name = "GetGames")]
    public async Task<ActionResult> GetAllGames()
    {
        var response=await _gameService.GetAllGames(); 
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost(Name = "PostGame")]
    public async Task<ActionResult> AddGame(Game game)
    {
            var response=await _gameService.AddGame(game);
            return ConvertServiceResultToActionResult(response);
        
    }

    [HttpDelete]
    [Route("{id}")]
    public async Task<ActionResult> DeleteGame(string id)
    {

            var response=await _gameService.DeleteGame(id);
            return ConvertServiceResultToActionResult(response);

    }


    [HttpPut]
    public async Task<ActionResult> UpdateGame(Game game)
    {
           var response=await _gameService.UpdateGame(game);
            return ConvertServiceResultToActionResult(response);
    }
    
    private ActionResult ConvertServiceResultToActionResult<T>(ServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult); // Sukces
    }

    if (serviceResult.ResultCode is not null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(serviceResult);
}
}