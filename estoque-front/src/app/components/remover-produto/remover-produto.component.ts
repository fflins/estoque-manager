import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProdutoService } from '../../services/produto.service';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-remover-produto',
  templateUrl: './remover-produto.component.html',
  styleUrl: './remover-produto.component.css',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
})

export class RemoverProdutoComponent {
  removerForm: FormGroup;
  feedbackMessage: string = '';

  constructor(private fb: FormBuilder, private produtoService: ProdutoService, private location: Location) {
    this.removerForm = this.fb.group({
      codigo: ['', Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]],  // Quantidade a ser removida
    });
    
  }

  onSubmit() {
    const codigo = this.removerForm.get('codigo')?.value;
    const quantidade = this.removerForm.get('quantidade')?.value;
  
    if (!quantidade || quantidade <= 0) {
      this.feedbackMessage = 'Informe uma quantidade válida para remover';
      return;
    }
  
    this.produtoService.removerProduto(codigo, quantidade).subscribe({
      next: (response) => {
        console.log('Remoção realizada:', response); // Log para depuração
        this.feedbackMessage = response.message; // Exibe a mensagem de sucesso
        this.removerForm.reset(); // Limpa o formulário após submissão
      },
      error: (err) => {
        console.error('Erro na remoção:', err); // Log de erro para depuração
        this.feedbackMessage = err.error?.message || 'Erro ao remover produto';
      }
    });
  }

  
  voltar(): void {
    this.location.back();
  }


}
