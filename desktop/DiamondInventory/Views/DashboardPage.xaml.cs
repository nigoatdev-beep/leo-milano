using DiamondInventory.Models;
using DiamondInventory.ViewModels;
using Microsoft.UI.Xaml;
using Microsoft.UI.Xaml.Controls;
using Microsoft.UI.Xaml.Input;

namespace DiamondInventory.Views;

public sealed partial class DashboardPage : Page
{
    public DashboardViewModel ViewModel { get; } = new();

    public DashboardPage()
    {
        InitializeComponent();
        Loaded += (_, _) => ViewModel.Load();
        DiamondsRepeater.ElementPrepared += OnItemPrepared;
    }

    private void OnItemPrepared(ItemsRepeater sender, ItemsRepeaterElementPreparedEventArgs args)
    {
        if (args.Element is FrameworkElement element && element.DataContext is Diamond diamond)
        {
            element.PointerPressed += (_, e) =>
            {
                if (e.GetCurrentPoint(element).Properties.IsRightButtonPressed)
                    ShowContextMenu(diamond, element);
                else
                    (App.MainWindow as MainWindow)?.NavigateToEdit(diamond);
            };
        }
    }

    private void ShowContextMenu(Diamond diamond, FrameworkElement target)
    {
        var menu = new MenuFlyout();
        menu.Items.Add(new MenuFlyoutItem { Text = "Modifier" });
        menu.Items.Add(new MenuFlyoutItem { Text = "Supprimer" });
        if (menu.Items[1] is MenuFlyoutItem delete)
            delete.Click += (_, _) => ViewModel.DeleteCommand.Execute(diamond.Id);
        menu.ShowAt(target);
    }
}
