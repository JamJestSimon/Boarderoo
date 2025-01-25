using BoarderooAPI.Service;
using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;
var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Set environment variable for Google credentials (use private key json file’s path)
var credentialPath = Path.Combine(Directory.GetCurrentDirectory(), "Credentials", "serviceAccountKey.json");
System.Environment.SetEnvironmentVariable("GOOGLE_APPLICATION_CREDENTIALS", credentialPath);

// Initialize Firebase Admin SDK
FirebaseApp.Create(new AppOptions()
{
    Credential = GoogleCredential.GetApplicationDefault()
});

// Add Firestore DB Service (use Project ID)
builder.Services.AddSingleton(FirestoreDb.Create("boarderoo-71469"));
builder.Services.AddScoped<FireBaseService>();
builder.Services.AddScoped<LoginService>();
builder.Services.AddScoped<RegisterService>();
builder.Services.AddScoped<UserService>();
builder.Services.AddScoped<AdminService>();
builder.Services.AddScoped<GameService>();
builder.Services.AddScoped<OrderService>();
builder.Services.AddScoped<EmailService>();
builder.Services.AddScoped<DiscordService>();
builder.Services.AddScoped<GoogleService>();
builder.Services.AddScoped<FileService>();
builder.Services.AddControllers();
builder.Services.AddCors(opt => 
{
    opt.AddPolicy("CorsPolicy",policyBuilder =>
    {
        policyBuilder.AllowAnyHeader().AllowAnyMethod().WithOrigins("http://localhost:4200",
        "",
        "https://discord.com",
        "https://oauth2.googleapis.com",
        "https://api.mailersend.com",
        "https://boarderoo-71469.firebaseapp.com"
        );
    });

});
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<IConfiguration>(builder.Configuration);
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();
app.UseCors("CorsPolicy");


var port = Environment.GetEnvironmentVariable("PORT") ?? "8080";
app.Run($"http://0.0.0.0:{port}");
