import { Component } from '@angular/core';
import { ProdutoService } from '../../services/produto.service';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-inserir-produto',
  templateUrl: './inserir-produto.component.html',
  styleUrls: ['./inserir-produto.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule]
})
export class InserirProdutoComponent {
  produtoForm: FormGroup;

  constructor(private produtoService: ProdutoService, private fb: FormBuilder) {
    this.produtoForm = this.fb.group({
      nome: ['', Validators.required],
      descricao: ['', Validators.required],
      codigo: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.produtoForm.valid) {
      this.produtoService.inserirProduto(this.produtoForm.value).subscribe(
        (response) => {
          alert('Produto inserido com sucesso!');
          this.produtoForm.reset();
        },
        (error) => {
          alert('Erro ao inserir produto!');
          console.error(error);
        }
      );
    }
  }
}
