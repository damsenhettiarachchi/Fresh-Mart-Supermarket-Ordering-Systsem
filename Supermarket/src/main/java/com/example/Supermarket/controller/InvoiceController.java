package com.example.Supermarket.controller;


import com.example.Supermarket.dto.InvoiceDTO;
import com.example.Supermarket.entity.Invoice;
import com.example.Supermarket.repository.InvoiceRepository;
import com.example.Supermarket.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

import java.util.Map;
import java.util.LinkedHashMap;
import java.time.Month;


    @Controller
    public class InvoiceController {

        private final InvoiceRepository invoiceRepository;

        public InvoiceController(InvoiceRepository invoiceRepository) {
            this.invoiceRepository = invoiceRepository;
        }

        // Redirect root to dashboard
        @GetMapping("/")
        public String redirectToDashboard() {
            return "redirect:/dashboard";
        }

        // Dashboard route
        @GetMapping("/dashboard")
        public String showDashboard(Model model) {
            return listInvoices(model);
        }

        // Show Create Invoice Form
        @GetMapping("/invoice/create")
        public String showCreateForm(Model model) {
            model.addAttribute("invoice", new Invoice());
            model.addAttribute("mainTitle", "Create New Invoice");
            model.addAttribute("userName", "User");
            model.addAttribute("alertCount", 3);
            return "invoice-form"; // Name of your HTML file (invoice-form.html)
        }

        // New Invoice route (matches dashboard button)
        @GetMapping("/invoice/new")
        public String showNewInvoiceForm(Model model) {
            return showCreateForm(model);
        }

        // Handle Create & Update
        @PostMapping("/invoice/save")
        public String saveInvoice(@Valid @ModelAttribute Invoice invoice, BindingResult bindingResult, Model model) {
            if (bindingResult.hasErrors()) {
                // If validation fails, return to the form with error messages
                model.addAttribute("mainTitle", invoice.getId() == null ? "Create New Invoice" : "Edit Invoice");
                model.addAttribute("userName", "User");
                model.addAttribute("alertCount", 3);
                return "invoice-form";
            }

            invoiceRepository.save(invoice);
            return "redirect:/dashboard"; // Redirect to dashboard
        }

        // List all invoices
        @GetMapping("/invoice/list")
        public String listInvoices(Model model) {
            List<Invoice> invoices = invoiceRepository.findAll();
                
            // Calculate statistics for the dashboard
            BigDecimal pendingPayments = invoices.stream()
                .filter(inv -> "Pending".equals(inv.getStatus()))
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            long pendingCount = invoices.stream()
                .filter(inv -> "Pending".equals(inv.getStatus()))
                .count();
                    
            BigDecimal paidToday = invoices.stream()
                .filter(inv -> "Paid".equals(inv.getStatus()) && 
                              inv.getDate() != null && 
                              inv.getDate().equals(java.time.LocalDate.now()))
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            long paidCount = invoices.stream()
                .filter(inv -> "Paid".equals(inv.getStatus()))
                .count();
                    
            long canceledCount = invoices.stream()
                .filter(inv -> "Cancelled".equals(inv.getStatus()))
                .count();
                
            model.addAttribute("invoices", invoices);
            model.addAttribute("mainTitle", "Invoice List");
            model.addAttribute("userName", "Damsen");
            model.addAttribute("pendingPayments", pendingPayments);
            model.addAttribute("pendingInvoicesCount", pendingCount);
            model.addAttribute("paidToday", paidToday);
            model.addAttribute("paidCount", paidCount);
            model.addAttribute("canceledCount", canceledCount);
            model.addAttribute("alertCount", 3); // You can calculate this based on your business logic
                
            return "supermarket_dashboard"; // Create a simple table page for listing invoices
        }


        // Show Edit Form
        // Inside InvoiceController.java

        // NOTE: You must also change your method signature to include RedirectAttributes
        @GetMapping("/invoice/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

            // Attempt to find the invoice
            return invoiceRepository.findById(id)
                    .map(invoice -> {
                        // Success: Invoice found, proceed to show the form
                        model.addAttribute("userName", "User");
                        model.addAttribute("alertCount", 3);
                        model.addAttribute("invoice", invoice);
                        model.addAttribute("mainTitle", "Edit Invoice");
                        return "invoice-form";
                    })
                    .orElseGet(() -> {
                        // Failure: Invoice not found. Add error message and redirect to the list.
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: Invoice ID " + id + " was not found.");
                        return "redirect:/dashboard"; // Redirect to a safe page
                    });
        }

        // View Invoice Details
        @GetMapping("/invoice/view/{id}")
        public String viewInvoice(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
            return invoiceRepository.findById(id)
                    .map(invoice -> {
                        // Calculate additional fields for the view
                        BigDecimal subtotal = invoice.getAmount().divide(new BigDecimal("1.10"), 2, java.math.RoundingMode.HALF_UP);
                        BigDecimal tax = invoice.getAmount().subtract(subtotal);
                        
                        model.addAttribute("invoice", invoice);
                        model.addAttribute("mainTitle", "Invoice Details");
                        
                        // Add computed fields
                        model.addAttribute("subtotal", subtotal);
                        model.addAttribute("tax", tax);
                        
                        // Add default values for optional fields
                        model.addAttribute("customerAddress", "N/A");
                        model.addAttribute("customerEmail", "N/A");
                        model.addAttribute("dueDate", invoice.getDate().plusDays(14));
                        model.addAttribute("notes", "Thank you for your business. Payment is due within 14 days.");
                        
                        return "view-invoice";
                    })
                    .orElseGet(() -> {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error: Invoice ID " + id + " was not found.");
                        return "redirect:/dashboard";
                    });
        }

        // Delete Invoice
        @GetMapping("/invoice/delete/{id}")
        public String deleteInvoice(@PathVariable Long id) {
            invoiceRepository.deleteById(id);
            return "redirect:/dashboard";
        }






    }





