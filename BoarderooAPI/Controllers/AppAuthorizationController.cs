using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]
public class AppAuthorizationController:ControllerBase
{
     private readonly DiscordService _discordService;
     private readonly GoogleService _googleService;
     public AppAuthorizationController(DiscordService discordService, GoogleService googleService)
     {
        _discordService=discordService;
        _googleService=googleService;
     }

    [HttpPost]
     [Route("/discorduser")]
    public async Task<ActionResult> GetDiscordUserInfo([FromBody]LoginOAuthRequest token)
    {
        var tokenresponse =await _discordService.GetDiscordToken(token.Token);
       // var response=await _discordService.GetDiscordUserInfo(tokenresponse.Data);
        return ConvertServiceResultToActionResult(tokenresponse);

    }
    [HttpGet]
    [Route("/test")]
    public async Task<ActionResult> Test(string url)
    {
        try{
        using var client = new HttpClient();
        var response = await client.GetAsync("url");
        
        return Ok($"Response status: {response.Content.ReadAsStringAsync().Result}");
        }catch(Exception e)
        {
            return BadRequest("blad "+e);
        }
    }
    [HttpPost]
     [Route("/googleuser")]
    public async Task<ActionResult> GetGoogleUserInfo([FromBody]LoginOAuthRequest token)
    {
        var tokenresponse =await _googleService.GetGoogleToken(token.Token);
        //var response=await _googleService.GetGoogleUserInfo(tokenresponse.Data);
        return ConvertServiceResultToActionResult(tokenresponse);
    }

    private ActionResult ConvertServiceResultToActionResult<T>(ServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult); // Sukces
    }

    if (serviceResult.ResultCode is null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(serviceResult);
}
}
