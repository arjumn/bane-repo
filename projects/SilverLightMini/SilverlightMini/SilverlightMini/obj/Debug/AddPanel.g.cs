﻿#pragma checksum "D:\Courses\4-Spring 11\Web2.0\csci5980-evils\SilverLightMini\SilverlightMini\SilverlightMini\AddPanel.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "C071F7AD0A1BB3EC85DDC921625DB750"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.1
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using System;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Automation.Peers;
using System.Windows.Automation.Provider;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Interop;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Resources;
using System.Windows.Shapes;
using System.Windows.Threading;


namespace SilverlightMini {
    
    
    public partial class AddPanel : System.Windows.Controls.UserControl {
        
        internal System.Windows.Controls.Grid LayoutRoot;
        
        internal System.Windows.Controls.TextBox FirstName;
        
        internal System.Windows.Controls.TextBox LastName;
        
        internal System.Windows.Controls.TextBox Age;
        
        internal System.Windows.Controls.TextBox Salary;
        
        internal System.Windows.Controls.Button addButton;
        
        internal System.Windows.Controls.Button closeButton;
        
        internal System.Windows.Controls.Border border1;
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Windows.Application.LoadComponent(this, new System.Uri("/SilverlightMini;component/AddPanel.xaml", System.UriKind.Relative));
            this.LayoutRoot = ((System.Windows.Controls.Grid)(this.FindName("LayoutRoot")));
            this.FirstName = ((System.Windows.Controls.TextBox)(this.FindName("FirstName")));
            this.LastName = ((System.Windows.Controls.TextBox)(this.FindName("LastName")));
            this.Age = ((System.Windows.Controls.TextBox)(this.FindName("Age")));
            this.Salary = ((System.Windows.Controls.TextBox)(this.FindName("Salary")));
            this.addButton = ((System.Windows.Controls.Button)(this.FindName("addButton")));
            this.closeButton = ((System.Windows.Controls.Button)(this.FindName("closeButton")));
            this.border1 = ((System.Windows.Controls.Border)(this.FindName("border1")));
        }
    }
}