using System.Collections.Generic;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using DiamondInventory.Models;
using DiamondInventory.Services;

namespace DiamondInventory.ViewModels;

public partial class DashboardViewModel : ObservableObject
{
    private readonly DatabaseService _db = new();

    [ObservableProperty]
    private List<Diamond> _diamonds = new();

    [ObservableProperty]
    private bool _isEmpty = true;

    public DashboardViewModel()
    {
        Load();
    }

    public void Load()
    {
        Diamonds = _db.GetAll();
        IsEmpty = Diamonds.Count == 0;
    }

    [RelayCommand]
    private void Delete(long id)
    {
        _db.Delete(id);
        Load();
    }
}
