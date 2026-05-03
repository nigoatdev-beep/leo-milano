using DiamondInventory.Views;
using Microsoft.UI.Xaml;

namespace DiamondInventory;

public sealed partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        Width = 1200;
        Height = 800;
        ContentFrame.Navigate(typeof(DashboardPage));
    }

    private void OnAddClick(object sender, RoutedEventArgs e)
    {
        ContentFrame.Navigate(typeof(DiamondFormPage));
    }

    public void NavigateToEdit(DiamondInventory.Models.Diamond diamond)
    {
        ContentFrame.Navigate(typeof(DiamondFormPage), diamond);
    }

    public void NavigateBack() => ContentFrame.GoBack();
}
