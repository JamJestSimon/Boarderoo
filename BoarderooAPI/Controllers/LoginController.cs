using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]

 public class LoginController:ControllerBase
 {
    private readonly LoginService _loginService;
    public LoginController(LoginService loginService)
    {
        this._loginService=loginService;
    }

    [HttpGet(Name = "Login")]
    public async Task<ActionResult> Login(string email,string password)
    {
        var response=await _loginService.Login(email,password); 
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