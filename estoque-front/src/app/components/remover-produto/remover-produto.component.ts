import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProdutoService } from '../../services/produto.service';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-remover-produto',
  templateUrl: './remover-produto.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
})
export class RemoverProdutoComponent {
  removerForm: FormGroup;
  feedbackMessage: string = '';

  constructor(private fb: FormBuilder, private produtoService: ProdutoService, private location: Location) {
    this.removerForm = this.fb.group({
      codigo: ['', Validators.required],
    });
  }

  onSubmit() {
    const codigo = this.removerForm.get('codigo')?.value;

    this.produtoService.removerProduto(codigo).subscribe({
      next: (response) => {
        this.feedbackMessage = response.message; // Mensagem de sucesso
      },
      error: (err) => {
        this.feedbackMessage = err.error.message || 'Erro ao remover produto'; // Mensagem de erro
      }
    });
  }

  voltar(): void {
    this.location.back();
  }


}
