using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Collections.ObjectModel;

namespace SilverlightMini
{
    public partial class MainPage : UserControl
    {
        private static ObservableCollection<Employee> FromSource;
        private static ObservableCollection<Employee> ToSource;

        private void InitializeFromTable()
        {
            int itemsCount = 100;

            for (int i = 0; i < itemsCount; i++)
            {
                Random randNum = new Random(i);
                FromSource.Add(new Employee()
                {
                    FirstName = "FirstName " + i + " ",
                    LastName = "LastName " + i,
                    Age = randNum.Next(20, 90) + "    ",
                    Salary = "$" + randNum.Next(20000, 50000) + "   "
                });
            }
        }

        public MainPage()
        {
            InitializeComponent();
            FromSource = new ObservableCollection<Employee>();
            ToSource = new ObservableCollection<Employee>();

            InitializeFromTable();
            this.dataGridFrom.ItemsSource = FromSource;
            this.dataGridTo.ItemsSource = ToSource;
        }

        private List<Employee> getSelectedRows(DataGrid dataGrid)
        {
            List<Employee> selectedRows = new List<Employee>();
            foreach (Employee selectedItem in dataGrid.SelectedItems)
                selectedRows.Add(selectedItem);
            return selectedRows;
        }

        private void buttonAdd_Click(object sender, RoutedEventArgs e)
        {
            foreach (Employee selectedRow in getSelectedRows(dataGridFrom))
            {
                ToSource.Add(selectedRow);
                FromSource.Remove(selectedRow);
            }
        }

        public static void addNewEmployee(Employee newEmployee)
        {
            FromSource.Add(newEmployee);
        }

        private void buttonRemove_Click(object sender, RoutedEventArgs e)
        {
            foreach (Employee selectedRow in getSelectedRows(dataGridTo))
            {
                FromSource.Add(selectedRow);
                ToSource.Remove(selectedRow);
            }
        }

        private List<Employee> getAllItems(ObservableCollection<Employee> source)
        {
            List<Employee> allElements = new List<Employee>();
            foreach (Employee employee in source)
            {
                allElements.Add(employee);
            }
            return allElements;
        }

        private void buttonAddAll_Click(object sender, RoutedEventArgs e)
        {

            foreach (Employee employee in FromSource)
            {
                ToSource.Add(employee);
            }
            FromSource.Clear();
        }

        private void buttonRemoveAll_Click(object sender, RoutedEventArgs e)
        {
            foreach (Employee employee in ToSource)
            {
                FromSource.Add(employee);
            }
            ToSource.Clear();
        }

        private void addNewButton_Click(object sender, RoutedEventArgs e)
        {
            this.addPanel.Visibility = System.Windows.Visibility.Visible;
        }
    }
}
