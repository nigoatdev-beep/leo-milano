using System;
using System.IO;
using DiamondInventory.Models;
using DiamondInventory.ViewModels;
using Microsoft.UI.Xaml;
using Microsoft.UI.Xaml.Controls;
using Microsoft.UI.Xaml.Input;
using Microsoft.UI.Xaml.Media.Imaging;
using Microsoft.UI.Xaml.Navigation;

namespace DiamondInventory.Views;

public sealed partial class DiamondFormPage : Page
{
    public DiamondFormViewModel ViewModel { get; private set; } = new();

    public DiamondFormPage()
    {
        InitializeComponent();
        UpdateImagePreview();
    }

    protected override void OnNavigatedTo(NavigationEventArgs e)
    {
        if (e.Parameter is Diamond diamond)
        {
            ViewModel = new DiamondFormViewModel(diamond);
            DataContext = ViewModel;
            Bindings.Update();
        }
        UpdateImagePreview();
    }

    private async void OnImageTapped(object sender, TappedRoutedEventArgs e)
    {
        await ViewModel.PickImageAsync(XamlRoot);
        UpdateImagePreview();
    }

    private async void OnImagePressed(object sender, PointerRoutedEventArgs e)
    {
        if (e.GetCurrentPoint((UIElement)sender).Properties.IsLeftButtonPressed)
        {
            await ViewModel.PickImageAsync(XamlRoot);
            UpdateImagePreview();
        }
    }

    private void UpdateImagePreview()
    {
        if (!string.IsNullOrEmpty(ViewModel.ImagePath) && File.Exists(ViewModel.ImagePath))
        {
            PreviewImage.Source = new BitmapImage(new Uri(ViewModel.ImagePath));
            PlaceholderText.Visibility = Visibility.Collapsed;
        }
        else
        {
            PreviewImage.Source = null;
            PlaceholderText.Visibility = Visibility.Visible;
        }
    }

    private void OnSave(object sender, RoutedEventArgs e)
    {
        ViewModel.Save();
        if (ViewModel.IsSaved)
            (App.MainWindow as MainWindow)?.NavigateBack();
    }

    private void OnCancel(object sender, RoutedEventArgs e)
    {
        (App.MainWindow as MainWindow)?.NavigateBack();
    }
}
