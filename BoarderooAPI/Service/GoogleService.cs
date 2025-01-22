using BoarderooAPI.Service;

public class GoogleService
{
    private readonly UserService _userService;
    public GoogleService(UserService userService)
    {
        _userService=userService;
    }
}