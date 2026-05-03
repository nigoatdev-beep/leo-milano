using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using DiamondInventory.Models;
using DiamondInventory.Services;
using Microsoft.UI.Xaml.Controls;
using Microsoft.UI.Xaml.Media.Imaging;
using Windows.Storage;
using Windows.Storage.Pickers;

namespace DiamondInventory.ViewModels;

public partial class DiamondFormViewModel : ObservableObject
{
    private readonly DatabaseService _db = new();
    private readonly Diamond? _existing;

    [ObservableProperty] private string _nom = "";
    [ObservableProperty] private string _carats = "";
    [ObservableProperty] private string _prix = "";
    [ObservableProperty] private string _couleur = "";
    [ObservableProperty] private string _clarte = "";
    [ObservableProperty] private string _notes = "";
    [ObservableProperty] private string _stock = "1";
    [ObservableProperty] private string? _imagePath;
    [ObservableProperty] private BitmapImage? _previewImage;
    [ObservableProperty] private string _title = "Nouveau Diamant";
    [ObservableProperty] private string _errorMessage = "";

    public long? DiamondId => _existing?.Id;
    public bool IsEdit => _existing != null;

    public DiamondFormViewModel(Diamond? diamond = null)
    {
        _existing = diamond;
        if (diamond != null)
        {
            Nom = diamond.Nom;
            Carats = diamond.Carats;
            Prix = diamond.Prix;
            Couleur = diamond.Couleur;
            Clarte = diamond.Clarte;
            Notes = diamond.Notes;
            Stock = diamond.Stock.ToString();
            ImagePath = diamond.ImagePath;
            Title = "Modifier Diamant";
            LoadPreview();
        }
    }

    private void LoadPreview()
    {
        if (!string.IsNullOrEmpty(ImagePath) && File.Exists(ImagePath))
        {
            PreviewImage = new BitmapImage(new Uri(ImagePath));
        }
    }

    public async Task PickImageAsync(XamlRoot xamlRoot)
    {
        var picker = new FileOpenPicker();
        picker.SuggestedStartLocation = PickerLocationId.PicturesLibrary;
        picker.FileTypeFilter.Add(".jpg");
        picker.FileTypeFilter.Add(".jpeg");
        picker.FileTypeFilter.Add(".png");
        WinRT.Interop.InitializeWithWindow.Initialize(picker, WinRT.Interop.WindowNative.GetWindowHandle(App.MainWindow));

        var file = await picker.PickSingleFileAsync();
        if (file != null)
        {
            var localFolder = ApplicationData.Current.LocalFolder;
            var imagesFolder = await localFolder.CreateFolderAsync("Images", CreationCollisionOption.OpenIfExists);
            var newFile = await file.CopyAsync(imagesFolder, $"{Guid.NewGuid()}{Path.GetExtension(file.Name)}", NameCollisionOption.GenerateUniqueName);
            ImagePath = newFile.Path;
            PreviewImage = new BitmapImage(new Uri(newFile.Path));
        }
    }

    [RelayCommand]
    private bool Save()
    {
        if (string.IsNullOrWhiteSpace(Nom))
        {
            ErrorMessage = "Le nom est obligatoire";
            return false;
        }

        var diamond = new Diamond
        {
            Id = _existing?.Id ?? 0,
            Nom = Nom.Trim(),
            Carats = Carats.Trim(),
            Prix = Prix.Trim(),
            Couleur = Couleur.Trim(),
            Clarte = Clarte.Trim(),
            Notes = Notes.Trim(),
            Stock = int.TryParse(Stock, out var s) ? s : 1,
            ImagePath = ImagePath,
            Timestamp = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds()
        };

        if (_existing != null)
            _db.Update(diamond);
        else
            _db.Insert(diamond);

        return true;
    }
}
