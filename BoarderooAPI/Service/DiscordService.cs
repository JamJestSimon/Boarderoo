
using BoarderooAPI.Service;

public class DiscordService
{
    private readonly UserService _userService;
    public DiscordService(UserService userService)
    {
        _userService=userService;
    }
}