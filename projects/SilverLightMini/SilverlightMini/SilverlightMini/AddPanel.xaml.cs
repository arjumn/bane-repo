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

namespace SilverlightMini
{
    public partial class AddPanel : UserControl
    {
        public AddPanel()
        {
            InitializeComponent();
        }

        private void closeButton_Click(object sender, RoutedEventArgs e)
        {
            this.Visibility = System.Windows.Visibility.Collapsed;
        }

        private void addButton_Click(object sender, RoutedEventArgs e)
        {
            Employee newEmployee = new Employee()
            {
                FirstName = this.FirstName.Text,
                LastName = this.LastName.Text,
                Salary = this.Salary.Text,
                Age = this.Age.Text,
            };
            MainPage.addNewEmployee(newEmployee);
            this.Visibility = System.Windows.Visibility.Collapsed;
        }
    }
}
