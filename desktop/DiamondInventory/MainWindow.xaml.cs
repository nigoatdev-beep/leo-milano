using DiamondInventory.Views;
using Microsoft.UI.Xaml;

namespace DiamondInventory;

public sealed partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
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
