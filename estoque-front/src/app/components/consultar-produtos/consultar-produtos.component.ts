import { Component, OnInit } from '@angular/core';
import { ProdutoService } from '../../services/produto.service';
import { Produto } from '../../models/produto.model';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-consultar-produtos',
  templateUrl: './consultar-produtos.component.html',
  styleUrls: ['./consultar-produtos.component.css'],
  standalone: true,
  imports: [CommonModule], 
})

export class ConsultarProdutosComponent implements OnInit {
  produtos: Produto[] = [];

  constructor(private produtoService: ProdutoService, private location: Location) {}

  ngOnInit(): void {
    this.consultarProdutos();
  }

  voltar(): void {
    this.location.back();
  }

  consultarProdutos() {
    this.produtoService.consultarProdutos().subscribe(produtos => {
      this.produtos = produtos;
    });
  }
}
